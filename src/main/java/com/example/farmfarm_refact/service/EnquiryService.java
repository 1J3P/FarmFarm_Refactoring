package com.example.farmfarm_refact.service;

import com.example.farmfarm_refact.apiPayload.ExceptionHandler;
import com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus;
import com.example.farmfarm_refact.controller.EnquiryController;
import com.example.farmfarm_refact.converter.EnquiryConverter;
import com.example.farmfarm_refact.dto.EnquiryRequestDto;
import com.example.farmfarm_refact.dto.EnquiryResponseDto;
import com.example.farmfarm_refact.entity.EnquiryEntity;
import com.example.farmfarm_refact.entity.ProductEntity;
import com.example.farmfarm_refact.entity.UserEntity;
import com.example.farmfarm_refact.repository.EnquiryRepository;
import com.example.farmfarm_refact.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnquiryService {

    @Autowired
    private EnquiryRepository enquiryRepository;

    @Autowired
    private ProductRepository productRepository;

    // 문의사항 등록
    public EnquiryResponseDto.EnquiryCreateResponseDto saveEnquiry(UserEntity user, Long pId, EnquiryRequestDto.EnquiryCreateRequestDto enquiryDto) {
        EnquiryEntity newEnquiry = new EnquiryEntity(enquiryDto.getContent(), user, "답변전");
        ProductEntity product = productRepository.findBypIdAndStatusLike(pId, "yes")
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.PRODUCT_NOT_FOUND));
        newEnquiry.setProduct(product);
        EnquiryEntity enquiry = enquiryRepository.save(newEnquiry);
        return EnquiryConverter.toEnquiryCreateResponseDto(enquiry);
    }

}
