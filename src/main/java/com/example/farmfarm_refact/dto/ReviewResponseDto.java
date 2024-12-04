package com.example.farmfarm_refact.dto;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

public class ReviewResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewCreateResponseDto {
        private Long rId;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewListDto {
        private Long rId;
        private String productName;
        private String comment;
        private Long productStar;
        private Long farmStar;
        private List<FileResponseDto.FileCreateResponseDto> images;
        private String nickname;
        private String profileImage;
        private Boolean isMyReview;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewListResponseDto {
        private List<ReviewResponseDto.ReviewListDto> reviewList;
    }
}
