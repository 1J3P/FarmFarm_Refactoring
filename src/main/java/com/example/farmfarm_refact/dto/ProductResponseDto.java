package com.example.farmfarm_refact.dto;

import com.example.farmfarm_refact.entity.ProductCategory;
import com.example.farmfarm_refact.entity.ShippingMethod;
import lombok.*;

import java.util.List;

@Getter
public class ProductResponseDto {


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductCreateResponseDto {
        private Long pId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductListDto {
        private Long pId;
        private String name;
        private int price;
        private List<FileResponseDto.FileCreateResponseDto> images;
        private FarmResponseDto.FarmReadResponseDto farm;
        private int productType;
        private String closeCalendar;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductListResponseDto {
        private List<ProductListDto> productList;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductReadResponseDto {
        private Long pId;
        private String name;
        private int price;
        private Double rating;
        private String detail;
        private List<FileResponseDto.FileCreateResponseDto> images;
        private FarmResponseDto.FarmReadResponseDto farm;
        private int quantity;
        private int productType;
        private ShippingMethod shippingMethod;
        private ProductCategory productCategory;
        private int groupProductQuantity;
        private int groupProductDiscount;
        private String closeCalendar;
        private Boolean isMyProduct;
    }

//    @Getter
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class ProductCategoryListDto {
//        private Long pId;
//        private String name;
//        private int price;
//        private List<FileResponseDto.FileCreateResponseDto> images;
//        private FarmResponseDto.FarmReadResponseDto farm;
//        private int productType;
//        private String closeCalendar;
//    }
//
//    @Getter
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class ProductCategoryListResponseDto {
//        private List<ProductCategoryListDto> productList;
//    }

}
