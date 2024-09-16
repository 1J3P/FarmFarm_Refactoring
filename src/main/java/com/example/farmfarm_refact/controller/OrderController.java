package com.example.farmfarm_refact.controller;


import com.example.farmfarm_refact.apiPayload.ApiResponse;
import com.example.farmfarm_refact.apiPayload.code.status.SuccessStatus;
import com.example.farmfarm_refact.dto.*;
import com.example.farmfarm_refact.entity.GroupEntity;
import com.example.farmfarm_refact.entity.OrderDetailEntity;
import com.example.farmfarm_refact.entity.ProductEntity;
import com.example.farmfarm_refact.entity.UserEntity;
import com.example.farmfarm_refact.service.FarmService;
import com.example.farmfarm_refact.service.GroupService;
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
    public ApiResponse<OrderResponseDto.OrderCartResponseDto> saveOrderDetailForCart(HttpSession session) {
        return ApiResponse.onSuccess(orderService.saveOrderDetailCart(session));
    }

    @PostMapping("")
    public ApiResponse<OrderResponseDto.OrderReadResponseDto> createOrder(@AuthenticationPrincipal UserEntity user, @RequestBody OrderRequestDto.OrderCreateRequestDto order, HttpSession session) {
        return ApiResponse.onSuccess(orderService.createOrder(user, session, order));
    }

    // 공구 개설하기 버튼 클릭시 (배송지 입력 폼으로 이동)
    @GetMapping("/createGroup/{pId}")
    public ApiResponse createGroup(@AuthenticationPrincipal UserEntity user, @PathVariable("pId") long pId, HttpSession session) {
        orderService.createGroup(user, pId, session);
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    // 공구 참여하기 버튼 클릭시 (배송지 입력 폼으로 이동)
    @GetMapping("/attendGroup/{gId}")
    public ApiResponse attendGroup(@AuthenticationPrincipal UserEntity user, @PathVariable("gId") long gId, HttpSession session) {
        orderService.attendGroup(user, gId, session);
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

//    // 경매 구매
    @PostMapping("/product/{pId}")
    public ApiResponse saveOrderDetailAuction(@AuthenticationPrincipal UserEntity user, @PathVariable("pId") long pId, @RequestBody OrderRequestDto.AuctionCreateRequestDto dto, HttpSession session) {
        orderService.saveOrderDetailAuction(user, pId, dto, session);
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    @DeleteMapping("/group/{gId}")
    public ApiResponse<PayResponseDto.refundPaymentDto> closeGroup(@PathVariable("gId") long gId) {
        return ApiResponse.onSuccess(orderService.closeGroupAndRefund(gId));
    }

}