package com.example.farmfarm_refact.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.Timestamp;
import java.util.List;

@Getter
public class FarmResponseDto {


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FarmCreateResponseDto {
        private Long fId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FarmReadResponseDto {
        private Long fId;
        private String name;
        private String locationCity;
        private String locationGu;
        private String locationFull;
        private String locationDetail;
        private String detail;
        private Double rating;
        private int auction_time;
        private boolean auction;
        private Timestamp created_at;
        private List<FileResponseDto.FileCreateResponseDto> images;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FarmListDto {
        private Long fId;
        private String name;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FarmListResponseDto {
        private List<FarmListDto> farmList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FarmUpdateResponseDto {
        private Long fId;
    }

}
