package com.example.farmfarm_refact.service;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.farmfarm_refact.apiPayload.ExceptionHandler;
import com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus;
import com.example.farmfarm_refact.converter.FarmConverter;
import com.example.farmfarm_refact.dto.FarmRequestDto;
import com.example.farmfarm_refact.dto.FarmResponseDto;
import com.example.farmfarm_refact.entity.FarmEntity;
import com.example.farmfarm_refact.entity.FileEntity;
import com.example.farmfarm_refact.entity.FileType;
import com.example.farmfarm_refact.entity.UserEntity;
import com.example.farmfarm_refact.repository.FarmRepository;
import com.example.farmfarm_refact.repository.FileRepository;
import com.example.farmfarm_refact.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus.S3_NOT_FOUND;

@Service
public class FarmService {
    @Autowired
    private FarmRepository farmRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private FileRepository fileRepository;

    // 농장 등록
    public FarmResponseDto.FarmCreateResponseDto saveFarm(UserEntity user, FarmRequestDto.FarmCreateRequestDto farmCreateRequestDto) {
        FarmEntity newFarm = FarmConverter.toFarmEntity(farmCreateRequestDto);
        newFarm.setUser(user);
        newFarm.setStatus("yes");
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
    public FarmResponseDto.FarmReadResponseDto getFarm(Long fId) {
        FarmEntity farm = farmRepository.findByfIdAndStatusLike(fId, "yes")
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.FARM_NOT_FOUND));
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
    //나의 농장 조회
    public FarmResponseDto.FarmReadResponseDto getMyFarm(UserEntity user) {
        FarmEntity myFarm = farmRepository.findByUserAndStatusLike(user, "yes")
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.FARM_NOT_FOUND));;
        return FarmConverter.toFarmReadResponseDto(myFarm);
    }

    // 농장 수정
    public void updateFarm(FarmRequestDto.FarmUpdateRequestDto updateFarm) {
        FarmEntity oldFarm = farmRepository.findByfIdAndStatusLike(updateFarm.getFId(), "yes")
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.FARM_NOT_FOUND));
        FarmEntity newFarm = FarmConverter.toNewFarm(updateFarm);
        oldFarm.updateFarm(newFarm);
        farmRepository.save(oldFarm);
    }

    // 농장 삭제
    public void deleteFarm(UserEntity user, Long fId) {
        FarmEntity farm = farmRepository.findByfIdAndStatusLike(fId, "yes").orElseThrow(() -> new ExceptionHandler(ErrorStatus.FARM_NOT_FOUND));
        if (user.equals(farm.getUser())) {
            if (productService.getFarmProduct(FarmConverter.toFarmReadResponseDto(farm)) == null || productService.getFarmProduct(FarmConverter.toFarmReadResponseDto(farm)).getProductList().isEmpty()) { // 농장에 상품이 없으면
                System.out.println("농장에 상품이 없으므로 농장을 삭제합니다.");
                farm.setStatus("no");
                farmRepository.save(farm);
            }
            else
                throw new ExceptionHandler(ErrorStatus.FARM_HAS_PRODUCT);
        }
        else
            throw new ExceptionHandler(ErrorStatus.FARM_USER_NOT_EQUAL);
    }
}
