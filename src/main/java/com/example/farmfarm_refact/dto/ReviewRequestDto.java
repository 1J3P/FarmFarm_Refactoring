package com.example.farmfarm_refact.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
public class ReviewRequestDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewCreateRequestDto {
        private Long productStar;
        private Long farmStar;
        private String comment;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewUpdateRequestDto {
        @NotNull
        private Long rId;
        @NotNull
        private Long productStar;
        @NotNull
        private Long farmStar;
        @NotNull
        private String comment;
    }
}
