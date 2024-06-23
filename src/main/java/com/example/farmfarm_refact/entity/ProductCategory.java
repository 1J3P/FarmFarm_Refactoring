package com.example.farmfarm_refact.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProductCategory {
    FRUIT(1),
    VEGETABLE(2),
    ETC(3);
    private final Integer cateNum;
}
