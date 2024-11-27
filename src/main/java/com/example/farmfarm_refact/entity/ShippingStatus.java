package com.example.farmfarm_refact.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ShippingStatus {
    NO_DELIVERY(1),          // 배송 주문 아님
    PAYMENT_CONFIRMED(2),    // 입금 확인
    READY_TO_SHIP(3),        // 배송 준비
    IN_TRANSIT(4),           // 배송 중
    DELIVERED(5);            // 배송 완료
    private final Integer statusNum;
}
