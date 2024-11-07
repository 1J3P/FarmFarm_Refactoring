package com.example.farmfarm_refact.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewListResponseDto {
        private List<ReviewResponseDto.ReviewListDto> reviewList;
    }
}
