package com.example.farmfarm_refact.service;

import com.example.farmfarm_refact.apiPayload.ExceptionHandler;
import com.example.farmfarm_refact.converter.MyPageConverter;
import com.example.farmfarm_refact.dto.MyPageResponseDto;
import com.example.farmfarm_refact.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus.FARM_NOT_FOUND;

@Service
public class MyPageService {

    // 유저 이름, 농장 보여주기
    @Transactional
    public MyPageResponseDto.myPageResponseDto mypage(UserEntity user) {
        if (user.getFarm() == null) {
            return MyPageConverter.toMyPageResponseDto(user, false);
        }
        return MyPageConverter.toMyPageResponseDto(user, true);
    }
}
