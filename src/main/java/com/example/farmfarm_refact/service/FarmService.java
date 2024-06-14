package com.example.farmfarm_refact.service;


import com.example.farmfarm_refact.converter.FarmConverter;
import com.example.farmfarm_refact.dto.FarmRequestDto;
import com.example.farmfarm_refact.dto.FarmResponseDto;
import com.example.farmfarm_refact.entity.FarmEntity;
import com.example.farmfarm_refact.entity.UserEntity;
import com.example.farmfarm_refact.repository.FarmRepository;
import com.example.farmfarm_refact.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class FarmService {
    @Autowired
    private FarmRepository farmRepository;
    @Autowired
    private UserService userService;
//    @Autowired
//    private ProductService productService;

    // 농장 등록
    public FarmResponseDto.FarmCreateResponseDto saveFarm(UserEntity user, FarmRequestDto.FarmCreateRequestDto farmCreateRequestDto) {
        FarmEntity newFarm = FarmConverter.toFarmEntity(farmCreateRequestDto);
        newFarm.setUser(user);
        FarmEntity farm = farmRepository.save(newFarm);
        return FarmConverter.toFarmCreateResponseDto(farm);
    }

    // 농장 조회
    public FarmResponseDto.FarmReadResponseDto getFarm(Long fId) {
        FarmEntity farm = farmRepository.findByfIdAndStatusLike(fId, "yes");
        return FarmConverter.toFarmReadResponseDto(farm);
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
//    //나의 농장 조회
//    public FarmEntity getMyFarm(UserEntity user) {
//        FarmEntity myFarm = farmRepository.findByUserAndStatusLike(user, "yes");
//        if (myFarm != null) {
//            System.out.println(myFarm.getStatus());
//        }
//        return myFarm;
//    }
//
    // 농장 수정
    public void updateFarm(FarmRequestDto.FarmUpdateRequestDto updateFarm) {
        FarmEntity oldFarm = farmRepository.findByfIdAndStatusLike(updateFarm.getFId(), "yes");
        FarmEntity newFarm = FarmConverter.toNewFarm(updateFarm);
        oldFarm.updateFarm(newFarm);
    }

    // 농장 삭제 *추후에 productService 구현 후 주석 해제 할 것. 절대 지우지 마시오!!*
//    public void deleteFarm(UserEntity user, Long fId) throws Exception {
//        FarmEntity farm = farmRepository.findByfIdAndStatusLike(fId, "yes");
//        if (user == farm.getUser()) {
//            if (productService.getFarmProduct(farm) == null || productService.getFarmProduct(farm).isEmpty()) { // 농장에 상품이 없으면
//                System.out.println("농장에 상품이 없으므로 농장을 삭제합니다.");
//                farm.setStatus("no");
//                farmRepository.save(farm);
//            }
//            else {
//                System.out.println("농장에 상품이 등록되어 있어 농장을 삭제할 수 없습니다.");
//                throw new Exception();
//            }
//        }
//        else {
//            System.out.println("현재 로그인 한 사용자와 농장 주인이 달라 농장을 삭제할 수 없습니다.");
//            throw new Exception();
//        }
//    }
}
