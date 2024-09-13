package com.example.farmfarm_refact.dto;

import com.example.farmfarm_refact.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
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
        private Long oId;
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
        private Long odId;
        private Long price;
        private Integer quantity;
        private Integer type;
        private String farmName;
        private String productName;
        private Long totalPrice;
        private String fileUrl;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyOrderListDto {
        private Timestamp created_at;
        private PaymentStatus paymentStatus;
        private List<OrderResponseDto.OrderDetailReadResponseDto> orderDetails;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyOrderListResponseDto {
        private List<MyOrderListDto> myOrderList;
    }
}
