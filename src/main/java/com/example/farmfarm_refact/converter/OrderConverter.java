package com.example.farmfarm_refact.converter;


import com.example.farmfarm_refact.dto.*;
import com.example.farmfarm_refact.entity.FarmEntity;
import com.example.farmfarm_refact.entity.OrderDetailEntity;
import com.example.farmfarm_refact.entity.OrderEntity;

import java.util.List;
import java.util.stream.Collectors;


public class OrderConverter {

  // 요청
  public static OrderEntity toOrderEntity(OrderRequestDto.OrderCreateRequestDto orderCreateRequestDto) {

      return OrderEntity.builder()
              .delivery(orderCreateRequestDto.getIsDelivery())
              .deliveryName(orderCreateRequestDto.getDeliveryName())
              .deliveryPhone(orderCreateRequestDto.getDeliveryPhone())
              .deliveryAddress(orderCreateRequestDto.getDeliveryAddress())
              .deliveryAddressDetail(orderCreateRequestDto.getDeliveryAddressDetail())
              .deliveryMemo(orderCreateRequestDto.getDeliveryMemo())
              .build();
  }

//    public static OrderEntity toGroupOrderEntity(GroupRequestDto.GroupJoinRequestDto dto) {
//
//        return OrderEntity.builder()
//                .delivery(dto.getIsDelivery())
//                .deliveryName(dto.getDeliveryName())
//                .deliveryPhone(dto.getDeliveryPhone())
//                .deliveryAddress(dto.getDeliveryAddress())
//                .deliveryAddressDetail(dto.getDeliveryAddressDetail())
//                .deliveryMemo(dto.getDeliveryMemo())
//                .build();
//    }

  public static OrderResponseDto.OrderReadResponseDto toOrderReadResponseDto(OrderEntity order) {
      return OrderResponseDto.OrderReadResponseDto.builder()
              .oId(order.getOId())
              .isDelivery(order.isDelivery())
              .deliveryName(order.getDeliveryName())
              .deliveryPhone(order.getDeliveryPhone())
              .deliveryAddress(order.getDeliveryAddress())
              .deliveryAddressDetail(order.getDeliveryAddressDetail())
              .deliveryMemo(order.getDeliveryMemo())
              .orderDetails(OrderConverter.toOrderDetailDtoList(order.getOrderDetails()))
              .build();
  }

  public static OrderResponseDto.OrderDetailReadResponseDto toOrderDetailReadResponseDto(OrderDetailEntity orderDetail) {
      return OrderResponseDto.OrderDetailReadResponseDto.builder()
              .odId(orderDetail.getOdId())
              .type(orderDetail.getType())
              .price(orderDetail.getPrice())
              .quantity(orderDetail.getQuantity())
              .build();
  }


    // FarmEntity 리스트를 FarmListResponseDto로 변환하는 메서드
    public static List<OrderResponseDto.OrderDetailReadResponseDto> toOrderDetailDtoList(List<OrderDetailEntity> orderDetailList) {
        List<OrderResponseDto.OrderDetailReadResponseDto> orderDetailDtoList = orderDetailList.stream()
                .map(OrderConverter::toOrderDetailReadResponseDto)
                .collect(Collectors.toList());

        return orderDetailDtoList;
    }

    // update 시 필요한 메서드
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
