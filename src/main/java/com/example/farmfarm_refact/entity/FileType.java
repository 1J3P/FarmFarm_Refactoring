package com.example.farmfarm_refact.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FileType {
    FARM(1),
    PRODUCT(2);
    private final Integer typeNum;
}
