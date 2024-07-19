package com.example.farmfarm_refact.dto;

import com.example.farmfarm_refact.entity.FarmEntity;
import com.example.farmfarm_refact.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductListResponseDto {
        private List<ProductListDto> productList;
    }

    @Getter
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
        private int type;
        private boolean direct;
    }

}
