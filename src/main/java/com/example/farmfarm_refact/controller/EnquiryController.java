package com.example.farmfarm_refact.controller;

import com.example.farmfarm_refact.apiPayload.ApiResponse;
import com.example.farmfarm_refact.apiPayload.code.status.SuccessStatus;
import com.example.farmfarm_refact.dto.EnquiryRequestDto;
import com.example.farmfarm_refact.dto.EnquiryResponseDto;
import com.example.farmfarm_refact.entity.UserEntity;
import com.example.farmfarm_refact.service.EnquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/enquiry")
public class EnquiryController {

    @Autowired
    private EnquiryService enquiryService;

    //문의사항 등록
    @PostMapping("/{pId}")
    public ApiResponse<EnquiryResponseDto.EnquiryCreateResponseDto> createEnquiry(@AuthenticationPrincipal UserEntity user, @PathVariable Long pId, @RequestBody EnquiryRequestDto.EnquiryCreateRequestDto enquiry) {
        return ApiResponse.onSuccess(enquiryService.saveEnquiry(user, pId, enquiry));
    }

    //문의사항 수정
    @PatchMapping("/{eId}")
    public ApiResponse updateEnquiry(@AuthenticationPrincipal UserEntity user, @PathVariable Long eId, @RequestBody EnquiryRequestDto.EnquiryUpdateRequestDto enquiryUpdateRequestDto) {
        enquiryService.updateEnquiry(user, eId, enquiryUpdateRequestDto);
        return ApiResponse.onSuccess(SuccessStatus.LIMJANG_UPDATE);
    }

    //문의사항 삭제
    @DeleteMapping("/{eId}")
    public ApiResponse deleteEnquiry(@AuthenticationPrincipal UserEntity user, @PathVariable("eId") Long eId) {
        enquiryService.deleteEnquiry(user, eId);
        return ApiResponse.onSuccess(SuccessStatus.LIMJANG_DELETE);
    }

    //상품별 문의사항 조회
    @GetMapping("/{pId}")
    public ApiResponse<EnquiryResponseDto.EnquiryListResponseDto> getProductEnquiryList(@PathVariable Long pId) {
        return ApiResponse.onSuccess(enquiryService.getProductEnquiryList(pId));
    }


    //내가 쓴 문의사항 보기
    @GetMapping("/my")
    public ApiResponse<EnquiryResponseDto.EnquiryListResponseDto> getMyEnquiryList(@AuthenticationPrincipal UserEntity user) {
        return ApiResponse.onSuccess(enquiryService.getMyEnquiryList(user));
    }

    //관리자 페이지 문의관리
    @GetMapping("/admin")
    public ApiResponse<EnquiryResponseDto.EnquiryListResponseDto> getEnquiryAdminList() {
        return ApiResponse.onSuccess(enquiryService.getEnquiryAdminList());
    }

}
