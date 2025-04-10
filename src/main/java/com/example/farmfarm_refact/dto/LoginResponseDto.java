package com.example.farmfarm_refact.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponseDto {

    private String accessToken;
    private String refreshToken;
    private String email;
    private String nickname;

    @Builder
    public LoginResponseDto(String accessToken, String refreshToken, String email, String nickname) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.email = email;
        this.nickname = nickname;
    }

}