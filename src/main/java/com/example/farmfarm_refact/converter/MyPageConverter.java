package com.example.farmfarm_refact.converter;

import com.example.farmfarm_refact.dto.MyPageResponseDto;
import com.example.farmfarm_refact.entity.UserEntity;

public class MyPageConverter {

    public static MyPageResponseDto.myPageResponseDto toMyPageResponseDto(UserEntity user) {
        return MyPageResponseDto.myPageResponseDto.builder()
                .userName(user.getUsername())
                .farmName(user.getFarm().getName())
                .farmDetail(user.getFarm().getDetail())
                .build();
    }
}
