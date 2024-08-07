package com.example.farmfarm_refact.converter;

import com.example.farmfarm_refact.dto.GroupResponseDto;
import com.example.farmfarm_refact.dto.ProductResponseDto;
import com.example.farmfarm_refact.entity.GroupEntity;
import com.example.farmfarm_refact.entity.ProductEntity;

import java.util.List;
import java.util.stream.Collectors;

public class GroupConverter {

    // GroupEntity를 OpenedGroupDto로 변환하는 메서드
    public static GroupResponseDto.openedGroupDto toGroupDto(GroupEntity group) {
        return GroupResponseDto.openedGroupDto.builder()
                .nickname(group.getUser1().getNickname())
                .image(group.getUser1().getImage())
                .created_at(group.getCreated_at())
                .closed_at(group.getClosed_at())
                .stock(group.getStock())
                .isClose(group.getIsClose())
                .build();
    }

    // GroupEntity 리스트를 GroupListResponseDto로 변환하는 메서드
    public static GroupResponseDto.GroupListResponseDto toGroupList(List<GroupEntity> groupList) {
        List<GroupResponseDto.openedGroupDto> openedGroupList = groupList.stream()
                .map(GroupConverter::toGroupDto)
                .collect(Collectors.toList());

        return GroupResponseDto.GroupListResponseDto.builder()
                .groupList(openedGroupList)
                .build();
    }
}
