package com.example.farmfarm_refact.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class UserResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserGetResponseDto {

        private Long uId;
        private String nickname;
        private String email;
        private String image;
        private Long myFId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserNicknameGetResponseDto {

        private Long uId;
        private String nickname;
        private String email;
        private String image;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserUpdateProfileResponseDto {
        private String nickname;
        private String image;
    }
}
