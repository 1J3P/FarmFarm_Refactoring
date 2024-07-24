package com.example.farmfarm_refact.controller;


import com.example.farmfarm_refact.apiPayload.ApiResponse;
import com.example.farmfarm_refact.apiPayload.code.status.SuccessStatus;
import com.example.farmfarm_refact.dto.*;
import com.example.farmfarm_refact.entity.UserEntity;
import com.example.farmfarm_refact.service.FarmService;
import com.example.farmfarm_refact.service.OrderService;
import com.example.farmfarm_refact.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    //장바구니 페이지에서 주문하기 눌렀을때 호출되는 API - 오더 디테일 객체들 만들어서 session에 저장해주고, 직거래만 되는지 표시
    @GetMapping("/cart")
    public ApiResponse<OrderResponseDto.OrderCartResponseDto> saveOrderDetailForCart(@AuthenticationPrincipal UserEntity user, HttpSession session) {
        return ApiResponse.onSuccess(orderService.saveOrderDetailCart(session));
    }

    @PostMapping("")
    public ApiResponse<OrderResponseDto.OrderReadResponseDto> createOrder(@AuthenticationPrincipal UserEntity user, @RequestBody OrderRequestDto.OrderCreateRequestDto order, HttpSession session) {
        return ApiResponse.onSuccess(orderService.createOrder(user, session, order));
    }

    // 공동구매 첫 번째 참여자
    @GetMapping("/createGroup/{pId}")
    public ApiResponse<OrderResponseDto.OrderReadResponseDto> createGroup(@AuthenticationPrincipal UserEntity user, @PathVariable("pId") long pId, @RequestBody GroupRequestDto.GroupJoinRequestDto dto, HttpSession session) {
        return ApiResponse.onSuccess(orderService.createGroup(user, pId, dto, session));
    }

    // 공동구매 두 번째 참여자
    @GetMapping("/attendGroup/{gId}")
    public ApiResponse<OrderResponseDto.OrderReadResponseDto> attendGroup(@AuthenticationPrincipal UserEntity user, @PathVariable("gId") long gId, @RequestBody GroupRequestDto.GroupJoinRequestDto dto, HttpSession session) {
        return ApiResponse.onSuccess(orderService.attendGroup(user, gId, dto, session));
    }

    // 공동구매 24시간 후 닫히는 메소드
//    @DeleteMapping("/group/{gId}")
//    public String closeGroup(HttpSession session, HttpServletRequest request, @PathVariable("gId") long gId, Model model) {
//        UserEntity user = (UserEntity)session.getAttribute("user");
//        GroupEntity group = groupService.getGroup(gId);
//        System.out.println("GID : " + group.getGId());
//        List<OrderDetailEntity> orderdetails = group.getOrderDetails();
//        ProductEntity product = group.getProduct();
//        System.out.println(orderdetails);
//        OrderEntity order = orderdetails.get(0).getOrder();
//        System.out.println("OID : " + order.getOId());
//        ApprovePaymentEntity approvePayment = order.getPayment();
//        System.out.println("APID : " + approvePayment.getPaId());
//        ResponseEntity response = paymentController.refund(approvePayment.getPaId());
//        for (OrderDetailEntity od : orderdetails) {
//            //OrderDetailRepository.delete(od);
//        }
//        if (response.getStatusCode() == HttpStatus.OK) {
//            return "redirect:/product/"+product.getPId();
//        }
//        return "redirect:/index";
    }

}
