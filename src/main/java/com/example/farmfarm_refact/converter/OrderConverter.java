package com.example.farmfarm_refact.converter;


import com.example.farmfarm_refact.dto.*;
import com.example.farmfarm_refact.entity.AuctionEntity;
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
              .orderNumber(order.getOrderNumber())
              .invoiceNumber(order.getInvoiceNumber())
              .isDelivery(order.isDelivery())
              .paymentStatus(order.getPaymentStatus())
              .shippingStatus(order.getShippingStatus())
              .deliveryName(order.getDeliveryName())
              .deliveryPhone(order.getDeliveryPhone())
              .deliveryAddress(order.getDeliveryAddress())
              .deliveryAddressDetail(order.getDeliveryAddressDetail())
              .deliveryMemo(order.getDeliveryMemo())
              .orderDetails(OrderConverter.toOrderDetailDtoList(order.getOrderDetails()))
              .build();
  }

    public static OrderResponseDto.OrderDeliveryReadResponseDto toOrderDeliveryReadResponseDto(OrderEntity order) {
        return OrderResponseDto.OrderDeliveryReadResponseDto.builder()
                .oId(order.getOId())
                .orderNumber(order.getOrderNumber())
                .invoiceNumber(order.getInvoiceNumber())
                .isDelivery(order.isDelivery())
                .paymentStatus(order.getPaymentStatus())
                .shippingStatus(order.getShippingStatus())
                .deliveryName(order.getDeliveryName())
                .deliveryPhone(order.getDeliveryPhone())
                .deliveryAddress(order.getDeliveryAddress())
                .deliveryAddressDetail(order.getDeliveryAddressDetail())
                .deliveryMemo(order.getDeliveryMemo())
                .itemName(order.getPayment().getItem_name())
                .totalQuantity(order.getTotalQuantity())
                .totalPrice(order.getTotalPrice())
                .orderDetails(OrderConverter.toOrderDetailDtoList(order.getOrderDetails()))
                .build();
    }

  public static OrderResponseDto.OrderDetailReadResponseDto toOrderDetailReadResponseDto(OrderDetailEntity orderDetail) {
      return OrderResponseDto.OrderDetailReadResponseDto.builder()
              .odId(orderDetail.getOdId())
              .price(orderDetail.getPrice())
              .quantity(orderDetail.getQuantity())
              .type(orderDetail.getType())
              .farmName(orderDetail.getProduct().getFarm().getName())
              .productName(orderDetail.getProduct().getName())
              .totalPrice(orderDetail.getPrice() * orderDetail.getQuantity())
              .images(FileConverter.toFileCreateResponseDtoList(orderDetail.getProduct().getFiles()))
              .build();
  }


    // OrderDetailEntity 리스트를 OrderDetailReadResponseDto 리스트로 변환하는 메서드
    public static List<OrderResponseDto.OrderDetailReadResponseDto> toOrderDetailDtoList(List<OrderDetailEntity> orderDetailList) {
        List<OrderResponseDto.OrderDetailReadResponseDto> orderDetailDtoList = orderDetailList.stream()
                .map(OrderConverter::toOrderDetailReadResponseDto)
                .collect(Collectors.toList());

        return orderDetailDtoList;
    }

    public static List<OrderResponseDto.OrderDeliveryReadResponseDto> toOrderDeliveryDtoList(List<OrderEntity> orderList) {
        List<OrderResponseDto.OrderDeliveryReadResponseDto> orderDeliveryDtoList = orderList.stream()
                .map(OrderConverter::toOrderDeliveryReadResponseDto)
                .collect(Collectors.toList());

        return orderDeliveryDtoList;
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

    // OrderEntity를 변환하는 메서드
    public static OrderResponseDto.MyOrderListDto toOrderDto(OrderEntity order) {
        return OrderResponseDto.MyOrderListDto.builder()
                .created_at(order.getCreated_at())
                .paymentStatus(order.getPaymentStatus())
                .orderDetails(OrderConverter.toOrderDetailDtoList(order.getOrderDetails()))
                .build();
    }

    // 마이페이지 나의 주문 내역들
    public static OrderResponseDto.MyOrderListResponseDto toMyOrderList(List<OrderEntity> orderList) {
        List<OrderResponseDto.MyOrderListDto> myOrderList = orderList.stream()
                .map(OrderConverter::toOrderDto)
                .collect(Collectors.toList());

        return OrderResponseDto.MyOrderListResponseDto.builder()
                .myOrderList(myOrderList)
                .build();
    }

    // 마이페이지 나의 경매 내역들
    public static List<OrderResponseDto.AuctionOrderDetailResponseDto> toMyAuctionList(List<OrderDetailEntity> auctionList) {
        List<OrderResponseDto.AuctionOrderDetailResponseDto> myAuctionList = auctionList.stream()
                .map(OrderConverter::toAuctionOrderDetailDto)
                .collect(Collectors.toList());
        return myAuctionList;
    }

    public static OrderResponseDto.AuctionOrderDetailResponseDto toAuctionOrderDetailDto(OrderDetailEntity orderDetail) {
        return OrderResponseDto.AuctionOrderDetailResponseDto.builder()
                .odId(orderDetail.getOdId())
                .auId(orderDetail.getAuction().getAuId())
                .price((long) orderDetail.getAuction().getPrice())
                .quantity(orderDetail.getQuantity())
                .farmName(orderDetail.getProduct().getFarm().getName())
                .productName(orderDetail.getProduct().getName())
                .totalPrice(orderDetail.getOrder().getTotalPrice())
                .images(FileConverter.toFileCreateResponseDtoList(orderDetail.getProduct().getFiles()))
                .auctionStatus(orderDetail.getAuction().getStatus())
                .createdAt(orderDetail.getOrder().getCreated_at())
                .build();
    }
}
