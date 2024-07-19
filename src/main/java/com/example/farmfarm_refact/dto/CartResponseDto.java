package com.example.farmfarm_refact.dto;

import com.example.farmfarm_refact.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class CartResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemDto {   // 장바구니에 담긴 상품 정보를 나타내는 dto
        private long pId;
        private String farmName;
        private String productName;
        private int price;
        private int totalPrice;
        private int quantity;
        // 대표 이미지 1장 어떻게 받아와야 하지??
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemListResponseDto {
        private List<ItemDto> itemList;
    }
}
