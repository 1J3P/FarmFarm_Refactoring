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
    }

}
