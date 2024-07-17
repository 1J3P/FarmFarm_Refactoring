package com.example.farmfarm_refact.service;


import com.example.farmfarm_refact.apiPayload.ExceptionHandler;
import com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus;
import com.example.farmfarm_refact.apiPayload.exception.GeneralException;
import com.example.farmfarm_refact.controller.S3Controller;
import com.example.farmfarm_refact.converter.FarmConverter;
import com.example.farmfarm_refact.converter.OrderConverter;
import com.example.farmfarm_refact.dto.FarmRequestDto;
import com.example.farmfarm_refact.dto.FarmResponseDto;
import com.example.farmfarm_refact.dto.OrderRequestDto;
import com.example.farmfarm_refact.dto.OrderResponseDto;
import com.example.farmfarm_refact.entity.*;
import com.example.farmfarm_refact.entity.Cart.Cart;
import com.example.farmfarm_refact.entity.Cart.Item;
import com.example.farmfarm_refact.repository.*;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus.S3_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;


    public OrderResponseDto.OrderCartResponseDto saveOrderDetailCart(HttpSession session) {
        List<OrderDetailEntity> details = new ArrayList<>();
        Cart cart = (Cart)session.getAttribute("cart");
        int isDirect = 0; // 0 : 배송 가능, 1 : 직거래만
        for (Item i : cart.getItemList()) {
            OrderDetailEntity orderDetail = new OrderDetailEntity();
            orderDetail.setQuantity(i.getQuantity());
            ProductEntity product = productRepository.findById(i.getProduct().getPId())
                    .orElseThrow(() -> new ExceptionHandler(ErrorStatus.PRODUCT_NOT_FOUND));
            if (product.isDirect() == true) {   // 직거래만
                isDirect = 1;
            }
            orderDetail.setPrice(product.getPrice() * i.getQuantity());
            orderDetail.setType(product.getType());
            orderDetail.setProduct(product);
            details.add(orderDetail);
        }
        session.setAttribute("orderDetail", details);
        return new OrderResponseDto.OrderCartResponseDto(isDirect);
    }

    public OrderResponseDto.OrderReadResponseDto createOrder(UserEntity user, HttpSession session, OrderRequestDto.OrderCreateRequestDto dto) {
        List<OrderDetailEntity> details = (List<OrderDetailEntity>)session.getAttribute("orderDetail");
        OrderEntity order = OrderConverter.toOrderEntity(dto);
        order.setUser(user);
        int totalPrice = 0;
        int totalQuantity = 0;
        for (OrderDetailEntity d : details) {
            totalPrice += d.getPrice();
            totalQuantity += d.getQuantity();
        }
        if (order.isDelivery() == true) {
            totalPrice += 3000;
        }

        order.setTotalPrice(totalPrice);
        order.setTotalQuantity(totalQuantity);
        order.setPaymentStatus(PaymentStatus.BEFORE_PAYMENT);
        OrderEntity saveOrder = orderRepository.save(order);
        OrderEntity getOrder = orderRepository.findById(saveOrder.getOId())
                .orElseThrow(()->new ExceptionHandler(ErrorStatus.ORDER_NOT_FOUND));
        for (OrderDetailEntity d : details) {
            d.setOrder(getOrder);
            orderDetailRepository.save(d);
        }
        session.setAttribute("cart", null);
        return OrderConverter.toOrderReadResponseDto(getOrder);
    }

}
