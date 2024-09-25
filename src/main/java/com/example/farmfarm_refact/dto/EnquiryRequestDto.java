package com.example.farmfarm_refact.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EnquiryRequestDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnquiryCreateRequestDto {
        private String content;
    }

    public static class EnquiryUpdateRequestDto {

    }
}
