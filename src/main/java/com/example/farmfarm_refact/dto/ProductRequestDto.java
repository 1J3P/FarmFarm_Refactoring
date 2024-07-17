package com.example.farmfarm_refact.dto;

import com.example.farmfarm_refact.entity.ProductCategory;
import com.example.farmfarm_refact.entity.ShippingMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.List;


@Getter
public class ProductRequestDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductCreateRequestDto {
        private String name;
        private String detail;
        private Integer productType;
        private Integer quantity;
        private Integer price;
        private Date date;
        private Integer hour;
        private Integer minute;
        private ShippingMethod shippingMethod;
        private String directLocation;
        private ProductCategory productCategory;
        private List<Long> images;
        private Integer groupProductQuantity;
        private Integer groupProductDiscount;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductUpdateRequestDto {
        @NotNull
        private long pId;
        @NotBlank
        private String name;
        @NotNull
        private String detail;
        @NotNull
        private Integer quantity;
        @NotNull
        private Integer price;
        @NotNull
        private ShippingMethod shippingMethod;
        private String directLocation;
        private ProductCategory productCategory;
        private List<Long> addImages;
        private List<Long> deleteImages;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemDto {
        private Integer quantity;
    }

}
