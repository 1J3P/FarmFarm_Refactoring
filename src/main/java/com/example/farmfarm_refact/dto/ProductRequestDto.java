package com.example.farmfarm_refact.dto;

import com.example.farmfarm_refact.entity.ProductCategory;
import com.example.farmfarm_refact.entity.ShippingMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    }


}
