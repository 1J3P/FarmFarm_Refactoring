package com.example.farmfarm_refact.converter;

import com.example.farmfarm_refact.dto.CartRequestDto;
import com.example.farmfarm_refact.dto.CartResponseDto;
import com.example.farmfarm_refact.dto.ProductResponseDto;
import com.example.farmfarm_refact.entity.Cart.Item;

import java.util.List;
import java.util.stream.Collectors;

public class CartConverter {

    // Item을 ItemListDto로 변환하는 메서드
    public static CartResponseDto.ItemDto toItemDto(Item item) {
        return CartResponseDto.ItemDto.builder()
                .pId(item.getProduct().getPId())
                .farmName(item.getProduct().getFarm().getName())
                .productName(item.getProduct().getName())
                .price(item.getProduct().getPrice())
                .totalPrice(item.getProduct().getPrice() * item.getQuantity())
                .quantity(item.getQuantity())
                .build();
    }

    // Item 리스트를 ItemListResponseDto로 변환하는 메서드
    public static CartResponseDto.ItemListResponseDto toItemList(List<Item> itemList) {
        List<CartResponseDto.ItemDto> itemDtoList = itemList.stream()
                .map(CartConverter::toItemDto)
                .collect(Collectors.toList());

        return CartResponseDto.ItemListResponseDto.builder()
                .itemList(itemDtoList)
                .build();
    }
}
