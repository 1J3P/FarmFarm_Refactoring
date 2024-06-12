package com.example.farmfarm_refact.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FarmRequestDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FarmCreateRequestDto {
        private String name;
        private String locationCity;

        private String locationGu;

        private String locationFull;

        private String locationDetail;

        private String detail;

        private String image;

        private int auction_time;
    }

}
