package com.example.farmfarm_refact.converter;


import com.example.farmfarm_refact.dto.FarmRequestDto;
import com.example.farmfarm_refact.dto.FarmResponseDto;
import com.example.farmfarm_refact.dto.ProductRequestDto;
import com.example.farmfarm_refact.dto.ProductResponseDto;
import com.example.farmfarm_refact.entity.FarmEntity;
import com.example.farmfarm_refact.entity.ProductEntity;


public class ProductConverter {

  // 요청
//  public static ProductEntity toProductEntity(ProductRequestDto.ProductCreateRequestDto productCreateRequestDto) {
//
//      return ProductEntity.builder()
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
  public static ProductResponseDto.ProductCreateResponseDto toProductCreateResponseDto(ProductEntity product) {
      return ProductResponseDto.ProductCreateResponseDto.builder()
              .pId(product.getPId())
              .build();
  }
}
