package com.example.farmfarm_refact.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;


@Getter
public class OrderRequestDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderCreateRequestDto {
        private String deliveryName;
        private String deliveryPhone;
        private Boolean isDelivery;
        private String deliveryAddress;
        private String deliveryAddressDetail;
        private String deliveryMemo;
        private int quantity;   // 내가 구매할 공구 상품 수량
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuctionCreateRequestDto {
        private String deliveryName;
        private String deliveryPhone;
        private Boolean isDelivery;
        private String deliveryAddress;
        private String deliveryAddressDetail;
        private String deliveryMemo;
        private int quantity;   // 내가 구매할 상품 수량
        private int price;      // 내가 구매할 상품 가격
    }

}
