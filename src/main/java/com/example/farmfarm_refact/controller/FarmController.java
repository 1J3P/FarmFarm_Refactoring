package com.example.farmfarm_refact.controller;


import com.example.farmfarm_refact.apiPayload.ApiResponse;
import com.example.farmfarm_refact.apiPayload.ExceptionHandler;
import com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus;
import com.example.farmfarm_refact.apiPayload.code.status.SuccessStatus;
import com.example.farmfarm_refact.dto.*;
import com.example.farmfarm_refact.entity.FarmEntity;
import com.example.farmfarm_refact.entity.UserEntity;
import com.example.farmfarm_refact.entity.oauth.OauthToken;
import com.example.farmfarm_refact.repository.UserRepository;
import com.example.farmfarm_refact.service.FarmService;
import com.example.farmfarm_refact.service.ProductService;
import com.example.farmfarm_refact.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus.MEMBER_NOT_FOUND;
import static com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus.TOKEN_EMPTY;

@RestController
@RequestMapping("/farm")
public class FarmController {

    @Autowired
    private FarmService farmService;
    @Autowired
    private ProductService productService;

    // 농장 개설
    @PostMapping("")
    public ApiResponse<FarmResponseDto.FarmCreateResponseDto> createFarm(@AuthenticationPrincipal UserEntity user, @RequestBody FarmRequestDto.FarmCreateRequestDto farm) {
        return ApiResponse.onSuccess(farmService.saveFarm(user, farm));
    }

    // 농장 전체 조회, 농장 정렬
    @GetMapping("/list")
    public ApiResponse<FarmResponseDto.FarmListResponseDto> getFarmList(@RequestParam(required = false, defaultValue = "rating", value = "sort") String criteria,
                                                                        @RequestParam(required = false, defaultValue = "", value = "keyword") String keyword) {
        if (keyword.equals("")) {
            return ApiResponse.onSuccess(farmService.getFarmsOrderBy(criteria));
        }
        else {
            return ApiResponse.onSuccess(farmService.searchSortFarms(keyword, criteria));
        }
    }

    // 농장 조회 (전체 농장 리스트에서 클릭 시 해당 농장 페이지로 이동)
    @GetMapping("/{fId}")
    public ApiResponse<FarmResponseDto.FarmReadResponseDto> getFarm(@PathVariable("fId") long fId) {
        return ApiResponse.onSuccess(farmService.getFarm(fId));
    }

    // 나의 농장 조회
    @GetMapping("/my")
    public ApiResponse<FarmResponseDto.FarmReadResponseDto> getMyFarm(@AuthenticationPrincipal UserEntity user) {
        return ApiResponse.onSuccess(farmService.getMyFarm(user));
    }


    // 주문 내역 상태 변경


    // 농장별 상품 보기(일반 상품)
    @GetMapping("/{fId}/product")
    public ApiResponse<ProductResponseDto.ProductListResponseDto> getFarmProduct(@PathVariable("fId") long fId) {
        return ApiResponse.onSuccess(productService.getFarmProduct(farmService.getFarm(fId)));
    }

    // 농장별 상품 보기(공구 상품)
    @GetMapping("/{fId}/groupProduct")
    public ApiResponse<ProductResponseDto.ProductListResponseDto> getFarmGroupProduct(@PathVariable("fId") long fId) {
        return ApiResponse.onSuccess(productService.getFarmGroupProduct(farmService.getFarm(fId)));
    }


    // 농장별 상품 보기(경매 상품)
    @GetMapping("/{fId}/auctionProduct")
    public ApiResponse<ProductResponseDto.ProductListResponseDto> getFarmAuctionProduct(@PathVariable("fId") long fId) {
        return ApiResponse.onSuccess(productService.getFarmAuctionProduct(farmService.getFarm(fId)));
    }


    // 농장 정보 수정
    @PatchMapping("/{fId}")
    public ApiResponse updateFarm(@PathVariable Long fId, @RequestBody @Valid FarmRequestDto.FarmUpdateRequestDto farmUpdateRequestDto) {
        farmUpdateRequestDto.setFId(fId);
        farmService.updateFarm(farmUpdateRequestDto);
        return ApiResponse.onSuccess(SuccessStatus.LIMJANG_UPDATE);
    }


    // 농장 삭제
    @DeleteMapping("/{fId}")
    public ApiResponse deleteFarm(@AuthenticationPrincipal UserEntity user, @PathVariable("fId") long fId)  {
        farmService.deleteFarm(user, fId);
        return ApiResponse.onSuccess(SuccessStatus.LIMJANG_DELETE);
    }

    // 배송 조회 목록
    @GetMapping("/shippingList")
    public ApiResponse<List<OrderResponseDto.OrderDeliveryReadResponseDto>> getShippingList(@AuthenticationPrincipal UserEntity user) {
        return ApiResponse.onSuccess(farmService.getShippingList(user));
    }

    @PatchMapping("/shippingList/{oId}")
    public ApiResponse<FarmResponseDto.ShippingStatusUpdateResponseDto> changeShippingStatus(@AuthenticationPrincipal UserEntity user, @RequestBody FarmRequestDto.ShippingStatusUpdateRequestDto requestDto, @PathVariable("orderNumber") long oId) {
        return ApiResponse.onSuccess(farmService.changeShippingStatus(user, requestDto, oId));
    }

}
