package com.example.farmfarm_refact.converter;

import com.example.farmfarm_refact.dto.ReviewResponseDto;
import com.example.farmfarm_refact.entity.ReviewEntity;

public class ReviewConverter {

    public static ReviewResponseDto.ReviewCreateResponseDto toReviewCreateResponseDto(ReviewEntity review) {
        return ReviewResponseDto.ReviewCreateResponseDto.builder()
                .rId(review.getRId())
                .build();
    }
}
