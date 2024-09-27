package com.example.farmfarm_refact.converter;

import com.example.farmfarm_refact.dto.EnquiryRequestDto;
import com.example.farmfarm_refact.dto.EnquiryResponseDto;
import com.example.farmfarm_refact.dto.ProductRequestDto;
import com.example.farmfarm_refact.entity.EnquiryEntity;
import com.example.farmfarm_refact.entity.ProductEntity;

public class EnquiryConverter {

    public static EnquiryResponseDto.EnquiryCreateResponseDto toEnquiryCreateResponseDto(EnquiryEntity enquiry) {
        return EnquiryResponseDto.EnquiryCreateResponseDto.builder()
                .eId(enquiry.getEId())
                .build();
    }

    public static EnquiryEntity toNewEnquiry (EnquiryRequestDto.EnquiryUpdateRequestDto updateDto) {
        return EnquiryEntity.builder()
                .content(updateDto.getContent())
                .build();
    }
}
