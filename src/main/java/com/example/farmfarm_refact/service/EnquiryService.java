package com.example.farmfarm_refact.service;

import com.example.farmfarm_refact.apiPayload.ExceptionHandler;
import com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus;
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

import java.util.ArrayList;
import java.util.List;

import static com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus.ENQUIRY_USER_NOT_EQUAL;


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

    // 문의사항 수정
    public void updateEnquiry(UserEntity user, Long eId, EnquiryRequestDto.EnquiryUpdateRequestDto enquiryUpdateRequestDto) {
        EnquiryEntity oldEnquiry = enquiryRepository.findById(eId)
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.ENQUIRY_NOT_FOUND));
        EnquiryEntity newEnquiry = EnquiryConverter.toNewEnquiry(enquiryUpdateRequestDto);
        if (user.equals(oldEnquiry.getUser())) {
            oldEnquiry.updateEnquiry(newEnquiry);
            enquiryRepository.save(oldEnquiry);
        }
        else
            throw new ExceptionHandler(ENQUIRY_USER_NOT_EQUAL);
    }

    // 문의사항 삭제
    public void deleteEnquiry(UserEntity user, Long eId) {
        EnquiryEntity enquiry = enquiryRepository.findById(eId)
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.ENQUIRY_NOT_FOUND));
        if (user.equals(enquiry.getUser())) {
            enquiry.setStatus("문의삭제");
            enquiryRepository.save(enquiry);
        }
        else
            throw new ExceptionHandler(ENQUIRY_USER_NOT_EQUAL);
    }

    // 상품별 문의사항 조회
    public EnquiryResponseDto.EnquiryListResponseDto getProductEnquiryList(UserEntity user, Long pId) {
        ProductEntity product = productRepository.findBypIdAndStatusLike(pId, "yes")
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.PRODUCT_NOT_FOUND));
        List<EnquiryEntity> enquiryList = enquiryRepository.findAllByProductAndStatusNotLike(product, "문의삭제");
        EnquiryResponseDto.EnquiryListResponseDto dtoList = EnquiryConverter.toEnquiryList(enquiryList);
        for (EnquiryResponseDto.EnquiryListDto dto : dtoList.getEnquiryList()) {
            if (dto.getUsername().equals(user.getNickname())) {
                dto.setIsMyEnquiry(true);
            }
           else {
               dto.setIsMyEnquiry(false);
            }
        }
        return dtoList;
    }

    // 내가 쓴 문의사항 보기
    public EnquiryResponseDto.EnquiryListResponseDto getMyEnquiryList(UserEntity user) {
        List<EnquiryEntity> enquiryList = enquiryRepository.findAllByUserAndStatusNotLike(user, "문의삭제");
        return EnquiryConverter.toEnquiryList(enquiryList);
    }

    //관리자 페이지 문의관리
    public EnquiryResponseDto.EnquiryListResponseDto getEnquiryAdminList(UserEntity user) {
        List<EnquiryEntity> enquiryList = enquiryRepository.findAll();
        List<EnquiryEntity> resultList = new ArrayList<>();
        for (EnquiryEntity val : enquiryList) {
            if (val.getProduct().getFarm().getUser().equals(user)) {
                resultList.add(val);
            }
        }
        return EnquiryConverter.toEnquiryList(resultList);
    }

    //문의 답변 달기
    public EnquiryResponseDto.EnquiryReplyCreateResponseDto createEnquiryReply(Long eId, EnquiryRequestDto.EnquiryReplyCreateRequestDto enquiryDto) {
        EnquiryEntity enquiry = enquiryRepository.findById(eId)
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.ENQUIRY_NOT_FOUND));
        enquiry.setReply(enquiryDto.getReply());
        enquiry.setStatus("답변완료");
        enquiryRepository.save(enquiry);
        return EnquiryConverter.toEnquiryReplyCreateResponseDto(enquiry);
    }
}
