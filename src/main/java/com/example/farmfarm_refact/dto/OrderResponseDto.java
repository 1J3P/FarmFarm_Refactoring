package com.example.farmfarm_refact.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.Timestamp;
import java.util.List;

@Getter
public class OrderResponseDto {


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderCartResponseDto {
        private Integer isDirect;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderReadResponseDto {
        private Integer oId;
        private Long totalPrice;
        private Integer totalQuantity;
        private String paymentStatus;
        private Boolean isDelivery;
        private String deliveryAddress;
        private String deliveryAddressDetail;
        private String deliveryName;
        private String deliveryPhone;
        private String deliveryMemo;
        private List<OrderResponseDto.OrderDetailReadResponseDto> orderDetails;
    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderDetailReadResponseDto {
        private Integer odId;
        private Long price;
        private Integer quantity;
        private Integer type;
    }
}
