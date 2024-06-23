package com.example.farmfarm_refact.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ShippingMethod {
    DIRECT(1),
    DELIVERY(2);
    private final Integer methodNum;
}
