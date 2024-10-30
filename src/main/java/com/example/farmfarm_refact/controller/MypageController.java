package com.example.farmfarm_refact.controller;

import com.example.farmfarm_refact.apiPayload.ApiResponse;
import com.example.farmfarm_refact.dto.*;
import com.example.farmfarm_refact.entity.UserEntity;
import com.example.farmfarm_refact.service.FarmService;
import com.example.farmfarm_refact.service.MyPageService;
import com.example.farmfarm_refact.service.OrderService;
import com.example.farmfarm_refact.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mypage")
public class MypageController {

    @Autowired
    private MyPageService myPageService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private FarmService farmService;
    @Autowired
    private UserService userService;

    @GetMapping("")
    public ApiResponse<MyPageResponseDto.myPageResponseDto> mypage(@AuthenticationPrincipal UserEntity user) {
        return ApiResponse.onSuccess(myPageService.mypage(user));
    }

    // 나의 주문 내역
    @GetMapping("/orderList")
    public ApiResponse<OrderResponseDto.MyOrderListResponseDto> myOrderList(@AuthenticationPrincipal UserEntity user) {
        return ApiResponse.onSuccess(orderService.getMyOrderList(user));
    }

    //경매 참가 내역
    @GetMapping("/auctionList")
    public ApiResponse<List<OrderResponseDto.AuctionOrderDetailResponseDto>> myAuctionList(@AuthenticationPrincipal UserEntity user) {
        return ApiResponse.onSuccess(orderService.getMyAuctionList(user));
    }

    // 상품 후기 내역

    // 문의

    // 로그아웃

    // 프로필 관리
    @PostMapping("/profile")
    public ApiResponse<UserResponseDto.UserUpdateProfileResponseDto> updateProfile(@AuthenticationPrincipal UserEntity user, @RequestBody UserRequestDto.UserUpdateProfileRequestDto updateUser) {
        return ApiResponse.onSuccess(userService.updateProfile(user, updateUser));
    }

//    // 농장 관리
//    @GetMapping("/farm")
//    public ApiResponse<FarmResponseDto.FarmManageResponseDto> manageFarm(@AuthenticationPrincipal UserEntity user) {
//        return ApiResponse.onSuccess(farmService.manageFarm(user));
//    }
}
