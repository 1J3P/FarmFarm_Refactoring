package com.example.farmfarm_refact.converter;


import com.example.farmfarm_refact.dto.FarmRequestDto;
import com.example.farmfarm_refact.dto.FarmResponseDto;
import com.example.farmfarm_refact.dto.FileResponseDto;
import com.example.farmfarm_refact.entity.FarmEntity;
import com.example.farmfarm_refact.entity.FileEntity;

import java.util.List;
import java.util.stream.Collectors;


public class FileConverter {

  // 요청
//  public static FarmEntity toFarmEntity(FarmRequestDto.FarmCreateRequestDto farmCreateRequestDto) {
//
//      return FarmEntity.builder()
//              .name(farmCreateRequestDto.getName())
//              .locationCity(farmCreateRequestDto.getLocationCity())
//              .locationGu(farmCreateRequestDto.getLocationGu())
//              .locationFull(farmCreateRequestDto.getLocationFull())
//              .locationDetail(farmCreateRequestDto.getLocationDetail())
//              .detail(farmCreateRequestDto.getDetail())
//              .image(farmCreateRequestDto.getImage())
//              .auction_time(farmCreateRequestDto.getAuction_time())
//        .build();
//  }
  public static FileResponseDto.FileCreateResponseDto toFileCreateResponseDto(FileEntity file) {
      return FileResponseDto.FileCreateResponseDto.builder()
              .fileId(file.getFileId())
              .fileUrl(file.getFileurl())
              .build();
  }

    public static List<FileResponseDto.FileCreateResponseDto> toFileCreateResponseDtoList(List<FileEntity> files) {
        return files.stream()
                .map(FileConverter::toFileCreateResponseDto)
                .collect(Collectors.toList());
    }
}
