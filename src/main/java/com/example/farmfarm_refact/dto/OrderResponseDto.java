package com.example.farmfarm_refact.dto;

import com.example.farmfarm_refact.entity.AuctionStatus;
import com.example.farmfarm_refact.entity.PaymentStatus;
import com.example.farmfarm_refact.entity.ShippingStatus;
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
        private String orderNumber;
        private Long totalPrice;
        private Integer totalQuantity;
        private PaymentStatus paymentStatus;
        private ShippingStatus shippingStatus;
        private String invoiceNumber;
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
    public static class OrderDeliveryReadResponseDto {
        private Long oId;
        private String orderNumber;
        private Long totalPrice;
        private Integer totalQuantity;
        private PaymentStatus paymentStatus;
        private ShippingStatus shippingStatus;
        private String invoiceNumber;
        private Boolean isDelivery;
        private String deliveryAddress;
        private String deliveryAddressDetail;
        private String deliveryName;
        private String deliveryPhone;
        private String deliveryMemo;
        private String itemName;
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
        private List<FileResponseDto.FileCreateResponseDto> images;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuctionOrderDetailResponseDto {
        private Long odId;
        private Long auId;
        private Long price;
        private Integer quantity;
        private String farmName;
        private String productName;
        private Long totalPrice;
        private List<FileResponseDto.FileCreateResponseDto> images;
        private AuctionStatus auctionStatus;
        private Timestamp createdAt;
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
