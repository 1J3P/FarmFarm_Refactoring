package com.example.farmfarm_refact.dto;

import lombok.*;

public class EnquiryRequestDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnquiryCreateRequestDto {
        private String content;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnquiryUpdateRequestDto {
        private Long eId;
        private String content;
    }
}
