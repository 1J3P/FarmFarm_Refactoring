package com.example.farmfarm_refact.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MyPageResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class myPageResponseDto {
        private String userName;
        private String farmName;
        private String farmDetail;
    }
}
