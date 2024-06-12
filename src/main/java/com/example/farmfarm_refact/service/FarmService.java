package com.example.farmfarm_refact.service;


import com.example.farmfarm_refact.converter.FarmConverter;
import com.example.farmfarm_refact.dto.FarmRequestDto;
import com.example.farmfarm_refact.dto.FarmResponseDto;
import com.example.farmfarm_refact.entity.FarmEntity;
import com.example.farmfarm_refact.entity.UserEntity;
import com.example.farmfarm_refact.repository.FarmRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
//    public List<FarmEntity> getFarmsOrderBy(String criteria) {
//        switch (criteria) {
//            case "old":
//                return farmRepository.findAllByStatusLike(Sort.by(Sort.Direction.ASC, "fId"), "yes");
//            case "new":
//                return farmRepository.findAllByStatusLike(Sort.by(Sort.Direction.DESC, "fId"), "yes");
//            default:
//                return farmRepository.findAllByStatusLike(Sort.by(Sort.Direction.DESC, "rating"), "yes");
//        }
//    }
//
//    //농장 검색
//    public List<FarmEntity> searchFarms(String keyword) {
//        return farmRepository.findAllByNameContainingAndStatusLike(keyword, "yes");
//    }
//
//    //농장 검색, 농장 정렬 같이
//    public List<FarmEntity> searchSortFarms(String keyword, String criteria) {
//        switch (criteria) {
//            case "old":
//                return farmRepository.findAllByNameContainingAndStatusLike(keyword, Sort.by(Sort.Direction.ASC, "fId"), "yes");
//            case "new":
//                return farmRepository.findAllByNameContainingAndStatusLike(keyword, Sort.by(Sort.Direction.DESC, "fId"), "yes");
//            default:
//                return farmRepository.findAllByNameContainingAndStatusLike(keyword, Sort.by(Sort.Direction.DESC, "rating"), "yes");
//        }
//    }
//
//
//    public List<FarmEntity> searchByLocation(String locationCity, String locationGu) {
//        return farmRepository.findAllByLocationCityAndLocationGu(locationCity, locationGu);
//    }
//
//    // 농장 상세 조회
//    public FarmEntity getFarm(Long fId) {
//        FarmEntity fa = farmRepository.findByfIdAndStatusLike(fId, "yes");
//        if (fa.getStatus().equals("no"))
//            return null;
//        return fa;
//    }
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
//    // 농장 수정
//    public FarmEntity updateFarm(HttpServletRequest request, Long fId, FarmEntity farm) {
//        UserEntity user = userService.getUser(request);
//        FarmEntity newFarm = farmRepository.findByfIdAndStatusLike(fId, "yes");
//
//        if (Objects.equals(user.getUId(), newFarm.getUser().getUId())) {
//            // 수정되는 것들  (농장 이름, 위치-시, 위치-구, 상세, 이미지, 경매시간, 경매 참여 여부, 생성 시간?)
//            newFarm.setName(farm.getName());
//            newFarm.setLocationCity(farm.getLocationCity());
//            newFarm.setLocationGu(farm.getLocationGu());
//            newFarm.setLocationFull(farm.getLocationFull());
//            newFarm.setLocationDetail(farm.getLocationDetail());
//            newFarm.setDetail(farm.getDetail());
//            newFarm.setImage(farm.getImage());
//            newFarm.setAuction_time(farm.getAuction_time());
//            newFarm.setAuction(farm.isAuction()); // 이게 왜 이런걸까요 ?
//            newFarm.setCreated_at(farm.getCreated_at());
//            newFarm.setDetail(farm.getDetail());
//            farmRepository.save(newFarm);
//            return newFarm;
//        } else {
//            return null;
//        }
//    }
//
//    // 농장 삭제
//    public void deleteFarm(HttpSession session, Long fId) throws Exception {
//        UserEntity user = (UserEntity) session.getAttribute("user");
//        FarmEntity farm = farmRepository.findByfIdAndStatusLike(fId, "yes");
//        if (Objects.equals(user.getUId(), farm.getUser().getUId())) {
//            if (productService.getFarmProduct(farm) == null || productService.getFarmProduct(farm).isEmpty()) {  // 농장에 상품이 없으면
//                System.out.println("농장에 상품 없음!!!");
//                farm.setStatus("no");
//                farmRepository.save(farm);
//            }
//            else {
//                System.out.println("농장에 상품 있음!!!");
//                System.out.println("상품이 등록되어 있어 삭제할 수 없습니다.");
//                throw new Exception();
//            }
//        } else {
//            System.out.println("유저가 달라 삭제할 수 없습니다.");
//            throw new Exception();
//        }
//    }
}
