package com.example.farmfarm_refact.controller;

import com.example.farmfarm_refact.apiPayload.ApiResponse;
import com.example.farmfarm_refact.dto.MyPageResponseDto;
import com.example.farmfarm_refact.dto.OrderResponseDto;
import com.example.farmfarm_refact.entity.UserEntity;
import com.example.farmfarm_refact.service.MyPageService;
import com.example.farmfarm_refact.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mypage")
public class MypageController {

    @Autowired
    private MyPageService myPageService;
    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public ApiResponse<MyPageResponseDto.myPageResponseDto> mypage(@AuthenticationPrincipal UserEntity user) {
        return ApiResponse.onSuccess(myPageService.mypage(user));
    }

    // 나의 주문 내역
    @GetMapping("/orderList")
    public ApiResponse<OrderResponseDto.MyOrderListResponseDto> myOrderList(@AuthenticationPrincipal UserEntity user) {
        return ApiResponse.onSuccess(orderService.getMyOrderList(user));
    }

    // 경매 참가 내역
//    @GetMapping("/auctionList")
//    public ApiResponse<> myAuctionList(@AuthenticationPrincipal UserEntity user) {
//        return ApiResponse.onSuccess()
//    }

    // 상품 후기 내역

    // 문의

    // 로그아웃
}
