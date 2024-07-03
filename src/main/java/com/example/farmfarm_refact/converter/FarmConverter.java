package com.example.farmfarm_refact.converter;


import com.example.farmfarm_refact.dto.FarmRequestDto;
import com.example.farmfarm_refact.dto.FarmResponseDto;
import com.example.farmfarm_refact.entity.FarmEntity;
import org.springframework.data.repository.config.FragmentMetadata;

import java.util.List;
import java.util.stream.Collectors;


public class FarmConverter {

  // 요청
  public static FarmEntity toFarmEntity(FarmRequestDto.FarmCreateRequestDto farmCreateRequestDto) {

      return FarmEntity.builder()
              .name(farmCreateRequestDto.getName())
              .locationCity(farmCreateRequestDto.getLocationCity())
              .locationGu(farmCreateRequestDto.getLocationGu())
              .locationFull(farmCreateRequestDto.getLocationFull())
              .locationDetail(farmCreateRequestDto.getLocationDetail())
              .detail(farmCreateRequestDto.getDetail())
              .auction(farmCreateRequestDto.isAuction())
              .build();
  }
  public static FarmResponseDto.FarmCreateResponseDto toFarmCreateResponseDto(FarmEntity farm) {
      return FarmResponseDto.FarmCreateResponseDto.builder()
              .fId(farm.getFId())
              .build();
  }

  public static FarmResponseDto.FarmReadResponseDto toFarmReadResponseDto(FarmEntity farm) {
      return FarmResponseDto.FarmReadResponseDto.builder()
              .fId(farm.getFId())
              .name(farm.getName())
              .locationCity(farm.getLocationCity())
              .locationGu(farm.getLocationGu())
              .locationFull(farm.getLocationFull())
              .locationDetail(farm.getLocationDetail())
              .detail(farm.getDetail())
              .rating(farm.getRating())
              .images(FileConverter.toFileCreateResponseDtoList(farm.getFiles()))
              .build();
  }
    // FarmEntity를 FarmListDto로 변환하는 메서드
    public static FarmResponseDto.FarmListDto toFarmDto(FarmEntity farmEntity) {
        return FarmResponseDto.FarmListDto.builder()
                .fId(farmEntity.getFId())
                .name(farmEntity.getName())
                .build();
    }

    // FarmEntity 리스트를 FarmListResponseDto로 변환하는 메서드
    public static FarmResponseDto.FarmListResponseDto toFarmList(List<FarmEntity> farmList) {
        List<FarmResponseDto.FarmListDto> farmDtoList = farmList.stream()
                .map(FarmConverter::toFarmDto)
                .collect(Collectors.toList());

        return FarmResponseDto.FarmListResponseDto.builder()
                .farmList(farmDtoList)
                .build();
    }

    //
    public static FarmEntity toNewFarm(FarmRequestDto.FarmUpdateRequestDto updateDto) {
      return FarmEntity.builder()
              .name(updateDto.getName())
              .locationCity(updateDto.getLocationCity())
              .locationGu(updateDto.getLocationGu())
              .locationFull(updateDto.getLocationFull())
              .locationDetail(updateDto.getLocationDetail())
              .detail(updateDto.getDetail())
              .build();
    }

}
