package com.example.farmfarm_refact.dto;

import com.example.farmfarm_refact.entity.ShippingStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;


@Getter
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
        private List<Long> images;
        private boolean auction;
        private int auction_time;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FarmUpdateRequestDto {
        @NotNull
        private long fId;
        @NotBlank
        private String name;
        @NotNull
        private String locationCity;
        @NotNull
        private String locationGu;
        @NotNull
        private String locationFull;
        @NotNull
        private String locationDetail;
        @NotNull
        private String detail;
        private Boolean auction;
        private List<Long> addImages;
        private List<Long> deleteImages;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShippingStatusUpdateRequestDto {
        private ShippingStatus shippingStatus;
        private String invoiceNumber;

    }
}
