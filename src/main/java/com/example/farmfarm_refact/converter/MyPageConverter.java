package com.example.farmfarm_refact.converter;

import com.example.farmfarm_refact.dto.FileResponseDto;
import com.example.farmfarm_refact.dto.MyPageResponseDto;
import com.example.farmfarm_refact.dto.OrderResponseDto;
import com.example.farmfarm_refact.entity.UserEntity;

import java.util.List;

public class MyPageConverter {

//    public static MyPageResponseDto.myPageResponseDto toMyPageResponseDto(UserEntity user) {
//        return MyPageResponseDto.myPageResponseDto.builder()
//                .userName(user.getNickname())
//                .farmName(user.getFarm().getName())
//                .farmDetail(user.getFarm().getDetail())
//                .profileImage(user.getImage())
//                .farmImages(FileConverter.toFileCreateResponseDtoList(user.getFarm().getFiles()))
//                .build();
//    }

    public static MyPageResponseDto.myPageResponseDto toMyPageResponseDto(UserEntity user) {
        // FarmEntity가 null일 경우를 처리
        String farmName = (user.getFarm() != null) ? user.getFarm().getName() : null;
        String farmDetail = (user.getFarm() != null) ? user.getFarm().getDetail() : null;
        List<FileResponseDto.FileCreateResponseDto> farmImages = (user.getFarm() != null)
                ? FileConverter.toFileCreateResponseDtoList(user.getFarm().getFiles())
                : null;

        return MyPageResponseDto.myPageResponseDto.builder()
                .userName(user.getNickname())
                .farmName(farmName)  // farm이 null이면 null 반환
                .farmDetail(farmDetail)  // farm이 null이면 null 반환
                .profileImage(user.getImage())
                .farmImages(farmImages)  // farm이 null이면 null 반환
                .build();
    }

}
