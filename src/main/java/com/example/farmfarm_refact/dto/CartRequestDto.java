package com.example.farmfarm_refact.dto;

import lombok.*;

public class CartRequestDto {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemDto {   // 상품을 장바구니에 담을 때 수량 정보를 나타내는 dto
        private Integer quantity;
    }
}
