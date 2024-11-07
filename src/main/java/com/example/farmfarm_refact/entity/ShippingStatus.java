package com.example.farmfarm_refact.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ShippingStatus {
    NO_DELIVERY(1),          // 배송 주문 아님
    PAYMENT_CONFIRMED(2),    // 입금 확인
    IN_TRANSIT(3),           // 배송 중
    DELIVERED(4);            // 배송 완료
    private final Integer statusNum;
}
