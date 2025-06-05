package com.example.farmfarm_refact.service;


import com.example.farmfarm_refact.apiPayload.ExceptionHandler;
import com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus;
import com.example.farmfarm_refact.controller.S3Controller;
import com.example.farmfarm_refact.converter.FarmConverter;
import com.example.farmfarm_refact.converter.OrderConverter;
import com.example.farmfarm_refact.dto.FarmRequestDto;
import com.example.farmfarm_refact.dto.FarmResponseDto;
import com.example.farmfarm_refact.dto.OrderResponseDto;
import com.example.farmfarm_refact.entity.*;
import com.example.farmfarm_refact.repository.FarmRepository;
import com.example.farmfarm_refact.repository.FileRepository;
import com.example.farmfarm_refact.repository.OrderRepository;
import com.example.farmfarm_refact.repository.ProductRepository;
import com.example.farmfarm_refact.repository.ReviewRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus.FARM_USER_NOT_EQUAL;
import static com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus.S3_NOT_FOUND;

@Service
public class FarmService {
    @Autowired
    private FarmRepository farmRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private S3Controller s3Controller;
    @Autowired
    private FileService fileService;
    @Autowired
    private OrderRepository orderRepository;

    // 농장 등록
    public FarmResponseDto.FarmCreateResponseDto saveFarm(UserEntity user, FarmRequestDto.FarmCreateRequestDto farmCreateRequestDto) {
        if (farmRepository.findByUserAndStatusLike(user, "yes").isPresent()) {
            throw new ExceptionHandler(ErrorStatus.FARM_IS_PRESENT);
        }
        FarmEntity newFarm = FarmConverter.toFarmEntity(farmCreateRequestDto);
        newFarm.setUser(user);
        newFarm.setStatus("yes");
        if (newFarm.isAuction()) {
            newFarm.setAuction_time(farmCreateRequestDto.getAuction_time());
        }
        FarmEntity farm = farmRepository.save(newFarm);
        if (farmCreateRequestDto.getImages() != null) {
            for (Long imageId : farmCreateRequestDto.getImages()) {
                FileEntity file = fileRepository.findById(imageId.intValue())
                        .orElseThrow(() -> new ExceptionHandler(S3_NOT_FOUND));
                file.setFileType(FileType.FARM);
                file.setFarm(farm);
                fileRepository.save(file);
            }
        }
        return FarmConverter.toFarmCreateResponseDto(farm);
    }

    // 농장 조회
    public FarmResponseDto.FarmReadResponseDto getFarm(UserEntity user, Long fId) {
        FarmEntity farm = farmRepository.findByfIdAndStatusLike(fId, "yes")
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.FARM_NOT_FOUND));
        FarmResponseDto.FarmReadResponseDto dto = FarmConverter.toFarmReadResponseDto(farm);
        if (user != null && user.getUId() == farm.getUser().getUId()) {
            dto.setIsMyFarm(true);
        } else {
            dto.setIsMyFarm(false);
        }
        return dto;
    }

    //농장 전체 조회 및 정렬 (rating: 인기순 , old: 오래된 순, new: 신규순), Default: rating
    public FarmResponseDto.FarmListResponseDto getFarmsOrderBy(String criteria) {
        List<FarmEntity> farmList =
                switch (criteria) {
                    case "old" -> farmRepository.findAllByStatusLike(Sort.by(Sort.Direction.ASC, "fId"), "yes");
                    case "new" -> farmRepository.findAllByStatusLike(Sort.by(Sort.Direction.DESC, "fId"), "yes");
                    default -> farmRepository.findAllByStatusLike(Sort.by(Sort.Direction.DESC, "rating"), "yes");
                };

        return FarmConverter.toFarmList(farmList);
    }

    //농장 검색, 농장 정렬 같이
    public FarmResponseDto.FarmListResponseDto searchSortFarms(String keyword, String criteria) {
        List<FarmEntity> farmList =
                switch (criteria) {
                    case "old" -> farmRepository.findAllByNameContainingAndStatusLike(keyword, Sort.by(Sort.Direction.ASC, "fId"), "yes");
                    case "new" -> farmRepository.findAllByNameContainingAndStatusLike(keyword, Sort.by(Sort.Direction.DESC, "fId"), "yes");
                    default -> farmRepository.findAllByNameContainingAndStatusLike(keyword, Sort.by(Sort.Direction.DESC, "rating"), "yes");
                };

        return FarmConverter.toFarmList(farmList);
    }

    //농장 검색
    public List<FarmEntity> searchFarms(String keyword) {
        return farmRepository.findAllByNameContainingAndStatusLike(keyword, "yes");
    }
//
//
//
//    public List<FarmEntity> searchByLocation(String locationCity, String locationGu) {
//        return farmRepository.findAllByLocationCityAndLocationGu(locationCity, locationGu);
//    }
//
//
    //나의 농장 조회
    public FarmResponseDto.FarmReadResponseDto getMyFarm(UserEntity user) {
        FarmEntity myFarm = farmRepository.findByUserAndStatusLike(user, "yes")
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.FARM_NOT_FOUND));;
        return FarmConverter.toFarmReadResponseDto(myFarm);
    }


    // 농장 수정
    @Transactional
    public void updateFarm(FarmRequestDto.FarmUpdateRequestDto farmUpdateRequestDto) {
        FarmEntity farm = farmRepository.findByfIdAndStatusLike(farmUpdateRequestDto.getFId(), "yes")
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.FARM_NOT_FOUND));
        FarmEntity newFarm = FarmConverter.toNewFarm(farmUpdateRequestDto);
        farm.updateFarm(newFarm);
        farmRepository.save(farm);
        if (farmUpdateRequestDto.getAddImages() != null) {
            for (Long imageId : farmUpdateRequestDto.getAddImages()) {
                FileEntity file = fileRepository.findById(imageId.intValue())
                        .orElseThrow(() -> new ExceptionHandler(S3_NOT_FOUND));
                file.setFileType(FileType.FARM);
                file.setFarm(farm);
                fileRepository.save(file);
            }
        }
        if (farmUpdateRequestDto.getDeleteImages() != null) {
            for (Long imageId : farmUpdateRequestDto.getDeleteImages()) {
                s3Controller.deleteFile(imageId.intValue());
                fileService.deleteByFileId(imageId.intValue());
            }
        }
    }

    // 농장 삭제
    @Transactional
    public void deleteFarm(UserEntity user, Long fId) {
        FarmEntity farm = farmRepository.findByfIdAndStatusLike(fId, "yes").orElseThrow(() -> new ExceptionHandler(ErrorStatus.FARM_NOT_FOUND));
        if (user.equals(farm.getUser())) {
            if (productService.getFarmProduct(FarmConverter.toFarmReadResponseDto(farm)) == null || productService.getFarmProduct(FarmConverter.toFarmReadResponseDto(farm)).getProductList().isEmpty()) { // 농장에 상품이 없으면
                System.out.println("농장에 상품이 없으므로 농장을 삭제합니다.");

                List<FileEntity> files = farm.getFiles();
                for (FileEntity file : files) {
                    fileService.deleteByFileId(file.getFileId().intValue());
                }

                farm.setStatus("no");
                farmRepository.save(farm);
            }
            else
                throw new ExceptionHandler(ErrorStatus.FARM_HAS_PRODUCT);
        }
        else
            throw new ExceptionHandler(FARM_USER_NOT_EQUAL);
    }


    public List<OrderResponseDto.OrderDeliveryReadResponseDto> getShippingList (UserEntity user) {
        return OrderConverter.toOrderDeliveryDtoList(orderService.findOrdersByUser(user.getId()));
    }
    // 농장 관리
    public FarmResponseDto.FarmManageResponseDto manageFarm(UserEntity user) {
        // 농장이 존재하면 true, null이면 false
        boolean exist = farmRepository.findByUserAndStatusLike(user, "yes").isPresent();
        return FarmConverter.toFarmManageResponseDto(exist);

    }

    @Transactional
    public FarmResponseDto.ShippingStatusUpdateResponseDto changeShippingStatus(UserEntity user, FarmRequestDto.ShippingStatusUpdateRequestDto requestDto, long oId) {
        OrderEntity order = orderRepository.findById(oId)
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.ORDER_NOT_FOUND));
        if (order.getOrderDetails().get(0).getProduct().getFarm().getUser().getUId() == user.getUId()) {
            throw new ExceptionHandler(FARM_USER_NOT_EQUAL);
        }
        order.setShippingStatus(requestDto.getShippingStatus());
        if (requestDto.getInvoiceNumber() != null) {
            order.setInvoiceNumber(requestDto.getInvoiceNumber());
        }
        OrderEntity changedOrder = orderRepository.save(order);
        return FarmConverter.toShippingStatusUpdateResponseDto(changedOrder);
    }

    public void updateFarmRating(FarmEntity farm) {
        List<ProductEntity> products = productRepository.findByFarm(farm);
        List<ReviewEntity> allReviews = new ArrayList<>();
        for (ProductEntity product : products) {
            allReviews.addAll(reviewRepository.findBypId(product.getPId()));
        }

        if (allReviews.isEmpty()) return;

        double avg = allReviews.stream()
            .mapToDouble(ReviewEntity::getFarmStar)
            .average()
            .orElse(0.0);

        farm.setRating(avg);
        farmRepository.save(farm);
    }
}
