package com.example.farmfarm_refact.converter;

import com.example.farmfarm_refact.dto.EnquiryRequestDto;
import com.example.farmfarm_refact.dto.EnquiryResponseDto;
import com.example.farmfarm_refact.dto.ReviewRequestDto;
import com.example.farmfarm_refact.dto.ReviewResponseDto;
import com.example.farmfarm_refact.entity.EnquiryEntity;
import com.example.farmfarm_refact.entity.ReviewEntity;

import java.util.List;
import java.util.stream.Collectors;

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

    public static ReviewResponseDto.ReviewListDto toReviewDto(ReviewEntity review) {
        return ReviewResponseDto.ReviewListDto.builder()
                .rId(review.getRId())
                .productName(review.getOrderDetail().getProduct().getName())
                .comment(review.getComment())
                .productStar(review.getProductStar())
                .farmStar(review.getFarmStar())
                .images(FileConverter.toFileCreateResponseDtoList(review.getOrderDetail().getProduct().getFiles()))
                .nickname(review.getUser().getNickname())
                .profileImage(review.getUser().getImage())
                .build();
    }


    public static ReviewResponseDto.ReviewListResponseDto toReviewList(List<ReviewEntity> reviewList) {
        List<ReviewResponseDto.ReviewListDto> reviewDtoList = reviewList.stream()
                .map(ReviewConverter::toReviewDto)
                .collect(Collectors.toList());

        return ReviewResponseDto.ReviewListResponseDto.builder()
                .reviewList(reviewDtoList)
                .build();
    }
}
