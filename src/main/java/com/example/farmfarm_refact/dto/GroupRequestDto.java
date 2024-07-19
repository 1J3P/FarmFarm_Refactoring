package com.example.farmfarm_refact.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GroupRequestDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupCreateRequestDto {
        private String deliveryName;
        private String deliveryPhone;
        private Boolean isDelivery;
        private String deliveryAddress;
        private String deliveryAddressDetail;
        private String deliveryMemo;
        private int quantity;   // 내가 구매할 공구 상품 수량
    }

    public static class GroupAttendRequestDto {

    }
}