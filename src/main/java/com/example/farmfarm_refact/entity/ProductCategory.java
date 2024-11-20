package com.example.farmfarm_refact.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProductCategory {
    FRUIT(0),
    VEGETABLE(1),
    ETC(2);
    private final Integer cateNum;
}
