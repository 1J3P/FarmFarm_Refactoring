package com.example.farmfarm_refact.converter;

import com.example.farmfarm_refact.dto.MyPageResponseDto;
import com.example.farmfarm_refact.dto.OrderResponseDto;
import com.example.farmfarm_refact.entity.UserEntity;

public class MyPageConverter {

    public static MyPageResponseDto.myPageResponseDto toMyPageResponseDto(UserEntity user, Boolean isFarm) {
        return MyPageResponseDto.myPageResponseDto.builder()
                .userName(user.getNickname())
                .farmName(user.getFarm().getName())
                .farmDetail(user.getFarm().getDetail())
                .profileImage(user.getImage())
                .farmImages(FileConverter.toFileCreateResponseDtoList(user.getFarm().getFiles()))
                .isFarm(isFarm)
                .build();
    }

}
