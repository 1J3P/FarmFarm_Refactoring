package com.example.farmfarm_refact.controller;

import com.example.farmfarm_refact.apiPayload.ApiResponse;
import com.example.farmfarm_refact.dto.ReviewRequestDto;
import com.example.farmfarm_refact.dto.ReviewResponseDto;
import com.example.farmfarm_refact.entity.UserEntity;
import com.example.farmfarm_refact.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // 리뷰 등록
    @PostMapping("/{odId}")
    public ApiResponse<ReviewResponseDto.ReviewCreateResponseDto> createReview(@AuthenticationPrincipal UserEntity user, @PathVariable Long odId, @RequestBody ReviewRequestDto.ReviewCreateRequestDto review) {
        return ApiResponse.onSuccess(reviewService.saveReview(user, odId, review));
    }

    // 리뷰 수정



    // 리뷰 삭제


    // 상품별 리뷰 조회


    // 내가 쓴 리뷰 보기

}
