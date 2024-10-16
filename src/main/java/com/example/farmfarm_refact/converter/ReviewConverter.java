package com.example.farmfarm_refact.converter;

import com.example.farmfarm_refact.dto.EnquiryRequestDto;
import com.example.farmfarm_refact.dto.ReviewRequestDto;
import com.example.farmfarm_refact.dto.ReviewResponseDto;
import com.example.farmfarm_refact.entity.EnquiryEntity;
import com.example.farmfarm_refact.entity.ReviewEntity;

public class ReviewConverter {

    public static ReviewResponseDto.ReviewCreateResponseDto toReviewCreateResponseDto(ReviewEntity review) {
        return ReviewResponseDto.ReviewCreateResponseDto.builder()
                .rId(review.getRId())
                .build();
    }

    public static ReviewEntity toNewReview (ReviewRequestDto.ReviewUpdateRequestDto updateDto) {
        return ReviewEntity.builder()
                .productStar(updateDto.getProductStar())
                .farmStar(updateDto.getFarmStar())
                .comment(updateDto.getComment())
                .build();
    }
}
