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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.*;

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
    public ApiResponse<OrderResponseDto.OrderReadResponseDto> createGroup(@AuthenticationPrincipal UserEntity user, @PathVariable("pId") long pId, @RequestBody GroupRequestDto.GroupCreateRequestDto dto, HttpSession session) {
        return ApiResponse.onSuccess(orderService.createGroup(user, pId, dto, session));
    }

    // 공동구매 두 번째 참여자
//    @GetMapping("/attendGroup/{gId}")
//    public ApiResponse<OrderResponseDto.OrderReadResponseDto> attendGroup(@PathVariable("gId") long gId, @RequestBody GroupRequestDto.GroupAttendRequestDto dto, HttpSession session) {
//        return ApiResponse.onSuccess(orderService.attendGroup());
//    }

}
