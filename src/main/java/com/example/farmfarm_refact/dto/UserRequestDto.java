package com.example.farmfarm_refact.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class UserRequestDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSetNicknameRequestDto {
        private String nickname;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserUpdateProfileRequestDto {
        private String nickname;
        private String image;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserFindProfile {
        private String accessToken;
    }

    // 로그인 성능테스트용
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserFindEmail {
        private String email;
    }
}
