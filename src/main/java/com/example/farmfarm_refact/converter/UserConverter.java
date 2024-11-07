package com.example.farmfarm_refact.converter;

import com.example.farmfarm_refact.dto.UserResponseDto;
import com.example.farmfarm_refact.entity.UserEntity;

public class UserConverter {

    public static UserResponseDto.UserUpdateProfileResponseDto toUserUpdateResponseDto(UserEntity user) {
        return UserResponseDto.UserUpdateProfileResponseDto.builder()
                .nickname(user.getNickname())
                .image(user.getImage())
                .build();
    }
}
