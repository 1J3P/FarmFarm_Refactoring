package com.example.farmfarm_refact.controller;


import com.example.farmfarm_refact.apiPayload.ApiResponse;
import com.example.farmfarm_refact.apiPayload.ExceptionHandler;
import com.example.farmfarm_refact.apiPayload.code.status.SuccessStatus;
import com.example.farmfarm_refact.dto.FarmRequestDto;
import com.example.farmfarm_refact.dto.FarmResponseDto;
import com.example.farmfarm_refact.dto.LoginResponseDto;
import com.example.farmfarm_refact.dto.UserResponseDto;
import com.example.farmfarm_refact.entity.FarmEntity;
import com.example.farmfarm_refact.entity.UserEntity;
import com.example.farmfarm_refact.entity.oauth.OauthToken;
import com.example.farmfarm_refact.repository.UserRepository;
import com.example.farmfarm_refact.service.FarmService;
import com.example.farmfarm_refact.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import static com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus.TOKEN_EMPTY;

@RestController
@RequestMapping("/farm")
public class FarmController {

    @Autowired
    private FarmService farmService;

    // 농장 개설
    @PostMapping("")
    public ApiResponse<FarmResponseDto.FarmCreateResponseDto> createFarm(@AuthenticationPrincipal UserEntity user, @RequestBody FarmRequestDto.FarmCreateRequestDto farm) {
        return ApiResponse.onSuccess(farmService.saveFarm(user, farm));
    }

    // 농장 전체 조회, 농장 정렬
    @GetMapping("/list")
    public ApiResponse<FarmResponseDto.FarmListResponseDto> getFarmList(@RequestParam(required = false, defaultValue = "rating", value = "sort") String criteria,
                                                                        @RequestParam(required = false, defaultValue = "", value = "keyword") String keyword) {
        if (keyword.equals("")) {
            return ApiResponse.onSuccess(farmService.getFarmsOrderBy(criteria));
        }
        else {
            return ApiResponse.onSuccess(farmService.searchSortFarms(keyword, criteria));
        }
    }

    // 농장 조회 (전체 농장 리스트에서 클릭 시 해당 농장 페이지로 이동)
    @GetMapping("/{fId}")
    public ApiResponse<FarmResponseDto.FarmReadResponseDto> getFarm(@PathVariable("fId") long fId) {
        return ApiResponse.onSuccess(farmService.getFarm(fId));
    }

    // 나의 농장 조회


    // 주문 내역 상태 변경


    // 농장별 상품 보기


    // 농장 정보 수정


    // 농장 삭제
    @DeleteMapping("/{fId}")
    public ApiResponse deleteFarm(@AuthenticationPrincipal UserEntity user, @PathVariable("fId") long fId)  {
        farmService.deleteFarm(user, fId);
        return ApiResponse.onSuccess(SuccessStatus.LIMJANG_DELETE);
    }


    // gugun ??

}
