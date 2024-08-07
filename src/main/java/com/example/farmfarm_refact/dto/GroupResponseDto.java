package com.example.farmfarm_refact.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

public class GroupResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class openedGroupDto {
        private String nickname;
        private String image;
        private Timestamp created_at;
        private Timestamp closed_at;
        private int stock;
        private int isClose;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupListResponseDto {
        private List<GroupResponseDto.openedGroupDto> groupList;
    }
}
