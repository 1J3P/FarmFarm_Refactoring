package com.example.farmfarm_refact.converter;

import com.example.farmfarm_refact.dto.EnquiryRequestDto;
import com.example.farmfarm_refact.dto.EnquiryResponseDto;
import com.example.farmfarm_refact.dto.ProductRequestDto;
import com.example.farmfarm_refact.entity.EnquiryEntity;
import com.example.farmfarm_refact.entity.ProductEntity;

import java.util.List;
import java.util.stream.Collectors;

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

    public static EnquiryResponseDto.EnquiryListDto toEnquiryDto(EnquiryEntity enquiry) {
        return EnquiryResponseDto.EnquiryListDto.builder()
                .eId(enquiry.getEId())
                .username(enquiry.getUser().getUsername())
                .content(enquiry.getContent())
                .created_at(enquiry.getCreated_at())
                .productName(enquiry.getProduct().getName())
                .images(FileConverter.toFileCreateResponseDtoList(enquiry.getProduct().getFiles()))
                .status(enquiry.getStatus())
                .reply(enquiry.getReply())
                .build();
    }

    public static EnquiryResponseDto.EnquiryListResponseDto toEnquiryList(List<EnquiryEntity> enquiryList) {
        List<EnquiryResponseDto.EnquiryListDto> enquiryDtoList = enquiryList.stream()
                .map(EnquiryConverter::toEnquiryDto)
                .collect(Collectors.toList());

        return EnquiryResponseDto.EnquiryListResponseDto.builder()
                .enquiryList(enquiryDtoList)
                .build();
    }
}
