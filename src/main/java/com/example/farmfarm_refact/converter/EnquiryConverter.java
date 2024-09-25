package com.example.farmfarm_refact.converter;

import com.example.farmfarm_refact.dto.EnquiryResponseDto;
import com.example.farmfarm_refact.entity.EnquiryEntity;

public class EnquiryConverter {

    public static EnquiryResponseDto.EnquiryCreateResponseDto toEnquiryCreateResponseDto(EnquiryEntity enquiry) {
        return EnquiryResponseDto.EnquiryCreateResponseDto.builder()
                .eId(enquiry.getEId())
                .build();
    }
}
