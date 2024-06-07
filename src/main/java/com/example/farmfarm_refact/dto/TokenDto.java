package com.example.farmfarm_refact.dto;

import lombok.Getter;

@Getter
public class TokenDto {

    private final Long uId;

    public TokenDto(Long uId) {
        this.uId = uId;
    }

}
