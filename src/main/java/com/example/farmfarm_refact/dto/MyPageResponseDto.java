package com.example.farmfarm_refact.dto;

import com.example.farmfarm_refact.entity.FileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MyPageResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class myPageResponseDto {
        private String userName;
        private String farmName;
        private String farmDetail;
        private String profileImage;
        private List<FileResponseDto.FileCreateResponseDto> farmImages;
        private Boolean isFarm;
    }
}
