package com.example.farmfarm_refact.converter;

import com.example.farmfarm_refact.dto.PayResponseDto;
import com.example.farmfarm_refact.dto.ProductResponseDto;
import com.example.farmfarm_refact.entity.ProductEntity;
import com.example.farmfarm_refact.entity.kakaoPay.RefundPaymentEntity;

public class PayConverter {

    // RefundPaymentEntity를 dto로 변환하는 메서드
    public static PayResponseDto.refundPaymentDto toRefundPaymentDto(RefundPaymentEntity refund) {
        return PayResponseDto.refundPaymentDto.builder()
                .reId(refund.getReId())
                .build();
    }
}
