package com.example.farmfarm_refact.service;


import com.example.farmfarm_refact.apiPayload.ApiResponse;
import com.example.farmfarm_refact.apiPayload.ExceptionHandler;
import com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus;
import com.example.farmfarm_refact.controller.PaymentController;
import com.example.farmfarm_refact.converter.MyPageConverter;
import com.example.farmfarm_refact.converter.OrderConverter;
import com.example.farmfarm_refact.converter.PayConverter;
import com.example.farmfarm_refact.dto.*;
import com.example.farmfarm_refact.entity.*;
import com.example.farmfarm_refact.entity.Cart.Cart;
import com.example.farmfarm_refact.entity.Cart.Item;
import com.example.farmfarm_refact.entity.kakaoPay.RefundPaymentEntity;
import com.example.farmfarm_refact.repository.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.farmfarm_refact.entity.kakaoPay.ApprovePaymentEntity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final GroupService groupService;
    private final GroupRepository groupRepository;
    private final PaymentController paymentController;
    private final AuctionRepository auctionRepository;



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

        // 공구 주문일 경우 입력 받은 수량 값을 지정
        if (details.get(0).getProduct().getType() == 1) {
            totalQuantity = dto.getQuantity();
            totalPrice = totalQuantity * (int)details.get(0).getPrice();

            GroupEntity group = details.get(0).getGroup();
            group.setStock(group.getStock() - totalQuantity);
            groupRepository.save(group);

            details.get(0).setGroup(group);
        }
        // 일반 주문
        else {
            for (OrderDetailEntity d : details) {
                totalPrice += d.getPrice();
                totalQuantity += d.getQuantity();
            }
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
        session.setAttribute("orderDetail", null);
        return OrderConverter.toOrderReadResponseDto(getOrder);
    }

    public void createGroup(UserEntity user, Long pId, HttpSession session) {
        List<OrderDetailEntity> details = new ArrayList<>();
        ProductEntity product = productRepository.findBypIdAndStatusLike(pId, "yes")
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.PRODUCT_NOT_FOUND));
        GroupEntity group = groupService.createGroup(user, product);
        // created_at 컬럼에 저장된 시간을 가져옵니다.
        Timestamp createdAt = group.getCreated_at();
        // 24시간을 추가하여 closed_at 컬럼에 설정합니다.
        long twentyFourHoursInMillis = 24 * 60 * 60 * 1000; // 24시간을 밀리초로 표현
        Timestamp closedAt = new Timestamp(createdAt.getTime() + twentyFourHoursInMillis);
        group.setClosed_at(closedAt);
        groupRepository.save(group);

        // Order detail 생성
        OrderDetailEntity orderDetail = new OrderDetailEntity();
        orderDetail.setGroup(group);
        orderDetail.setProduct(group.getProduct());
        orderDetail.setType(1);
//        orderDetail.setQuantity(dto.getQuantity());
        orderDetail.setPrice(product.getPrice() * (1 - product.getGroupProductDiscount() / 100));

//        // Order 생성
//        OrderEntity order = OrderConverter.toGroupOrderEntity(dto);
//        order.setUser(user);
//        int totalPrice = 0;
//        if (dto.getIsDelivery() == true) {
//            totalPrice += 3000;
//        }
//        order.setTotalPrice(totalPrice);
//        order.setTotalQuantity(dto.getQuantity());
//        order.setPaymentStatus(PaymentStatus.BEFORE_PAYMENT);
//        OrderEntity saveOrder = orderRepository.save(order);
//        OrderEntity getOrder = orderRepository.findById(saveOrder.getOId())
//                .orElseThrow(()->new ExceptionHandler(ErrorStatus.ORDER_NOT_FOUND));
//        orderDetail.setOrder(getOrder);
        orderDetailRepository.save(orderDetail);

        details.add(orderDetail);
        session.setAttribute("orderDetail", details);
//        return OrderConverter.toOrderReadResponseDto(getOrder);
    }

    public void attendGroup(UserEntity user, Long gId, HttpSession session) {
        List<OrderDetailEntity> details = new ArrayList<>();
        GroupEntity group = groupService.attendGroup(user, gId);
        ProductEntity product = group.getProduct();

        // Order detail 생성
        OrderDetailEntity orderDetail = new OrderDetailEntity();
        orderDetail.setGroup(group);
        orderDetail.setProduct(group.getProduct());
        orderDetail.setType(1);
        orderDetail.setQuantity(group.getStock());  // 공구 참여시에는 수량을 입력 받을 필요가 없음
        orderDetail.setPrice(product.getPrice() * (1 - product.getGroupProductDiscount() / 100));

//        // Order 생성
//        OrderEntity order = OrderConverter.toGroupOrderEntity(dto);
//        order.setUser(user);
//        int totalPrice = 0;
//        if (dto.getIsDelivery() == true) {
//            totalPrice += 3000;
//        }
//        order.setTotalPrice(totalPrice);
//        order.setTotalQuantity(dto.getQuantity());
//        order.setPaymentStatus(PaymentStatus.BEFORE_PAYMENT);
//        OrderEntity saveOrder = orderRepository.save(order);
//        OrderEntity getOrder = orderRepository.findById(saveOrder.getOId())
//                .orElseThrow(()->new ExceptionHandler(ErrorStatus.ORDER_NOT_FOUND));
//        orderDetail.setOrder(getOrder);
        orderDetailRepository.save(orderDetail);

        details.add(orderDetail);
        session.setAttribute("orderDetail", details);
//        return OrderConverter.toOrderReadResponseDto(getOrder);
    }

    // 공동구매 종료 및 환불(24시간 후 닫히는 메소드)
    public PayResponseDto.refundPaymentDto closeGroupAndRefund(Long gId) {
        GroupEntity group = groupService.getGroup(gId);
        group.setIsClose(1);
        group = groupRepository.save(group);
        List<OrderDetailEntity> orderdetails = group.getOrderDetails();
        ProductEntity product = group.getProduct();
        OrderEntity order = orderdetails.get(0).getOrder();
        ApprovePaymentEntity approvePayment = order.getPayment();
        ApiResponse<RefundPaymentEntity> response = paymentController.refund(approvePayment.getPaId());
        if (response.getIsSuccess() == true) {
            return PayConverter.toRefundPaymentDto(response.getResult());
        }
        else
            throw new ExceptionHandler(ErrorStatus.REFUND_FAIL);
    }

//    public OrderEntity getOrder(Long oId){
//        return orderRepository.findById(oId)
//                .orElseThrow(()->new ExceptionHandler(ErrorStatus.ORDER_NOT_FOUND));
//    }

    // 나의 주문내역
    public OrderResponseDto.MyOrderListResponseDto getMyOrderList(UserEntity user) {
        List<OrderEntity> myOrders = orderRepository.findAllByUser(user);
        return OrderConverter.toMyOrderList(myOrders);
    }

    public void saveOrderDetailAuction(UserEntity user, Long pId, OrderRequestDto.AuctionCreateRequestDto dto, HttpSession session) {
        ProductEntity product = productRepository.findById(pId)
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.PRODUCT_NOT_FOUND));
        List<OrderDetailEntity> details = new ArrayList<>();
        if (product.getShippingMethod() == ShippingMethod.DIRECT) {
            AuctionEntity auction = new AuctionEntity(dto.getQuantity(), dto.getPrice(), AuctionStatus.AUCTION_IN_PROGRESS, product, user);
            auction = auctionRepository.save(auction);
            OrderDetailEntity orderDetail = new OrderDetailEntity(auction.getQuantity(), (int)(long) auction.getPrice() * auction.getQuantity(), 2, product, auction);
            details.add(orderDetail);
            session.setAttribute("orderDetail", details);
        }
    }

}
