package com.example.farmfarm_refact.converter;


import com.example.farmfarm_refact.dto.FarmRequestDto;
import com.example.farmfarm_refact.dto.FarmResponseDto;
import com.example.farmfarm_refact.entity.FarmEntity;
import org.springframework.data.repository.config.FragmentMetadata;


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
              .image(farmCreateRequestDto.getImage())
              .auction_time(farmCreateRequestDto.getAuction_time())
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
              .image(farm.getImage())
              .build();
  }
}
