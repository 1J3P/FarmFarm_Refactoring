package com.example.farmfarm_refact.service;

import com.example.farmfarm_refact.apiPayload.ExceptionHandler;
import com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus;
import com.example.farmfarm_refact.controller.ProductController;
import com.example.farmfarm_refact.converter.FarmConverter;
import com.example.farmfarm_refact.converter.ProductConverter;
import com.example.farmfarm_refact.dto.*;
import com.example.farmfarm_refact.entity.*;
import com.example.farmfarm_refact.repository.FarmRepository;
import com.example.farmfarm_refact.repository.FileRepository;
import com.example.farmfarm_refact.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus.*;

@Service
public class ProductService {
    @Autowired
    private FarmRepository farmRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FileRepository fileRepository;

    // 상품 등록
    public ProductResponseDto.ProductCreateResponseDto saveProduct(UserEntity user, ProductRequestDto.ProductCreateRequestDto productCreateRequestDto) {
        ProductEntity newProduct = new ProductEntity(productCreateRequestDto.getName(), productCreateRequestDto.getDetail(), productCreateRequestDto.getProductType(), productCreateRequestDto.getProductCategory(), productCreateRequestDto.getShippingMethod(), "yes");
        FarmEntity myFarm = farmRepository.findByUserAndStatusLike(user, "yes").orElseThrow(() -> new ExceptionHandler(ErrorStatus.FARM_NOT_FOUND));
        newProduct.setFarm(myFarm);
        if (newProduct.getType() == 2) { //경매 상품이면
            if (myFarm.isAuction()) {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                cal.set(productCreateRequestDto.getDate().getYear() + 1900, productCreateRequestDto.getDate().getMonth(), productCreateRequestDto.getDate().getDate(), productCreateRequestDto.getHour(), productCreateRequestDto.getMinute());
                newProduct.setCloseCalendar(format.format(cal.getTime()));
                newProduct.setAuctionQuantity(productCreateRequestDto.getQuantity());
                newProduct.setLowPrice(productCreateRequestDto.getPrice());
            }
            else {
                throw new ExceptionHandler(FARM_AUCTION_FALSE) ;
            }
        } else {
            newProduct.setQuantity(productCreateRequestDto.getQuantity());
            newProduct.setPrice(productCreateRequestDto.getPrice());
        }
        if (newProduct.getShippingMethod() == ShippingMethod.DIRECT) {
            newProduct.setDirectLocation(productCreateRequestDto.getDirectLocation());
        }
        ProductEntity product = productRepository.save(newProduct);
        if (productCreateRequestDto.getImages() != null) {
            for (Long imageId : productCreateRequestDto.getImages()) {
                FileEntity file = fileRepository.findById(imageId.intValue())
                        .orElseThrow(() -> new ExceptionHandler(S3_NOT_FOUND));
                file.setFileType(FileType.PRODUCT);
                file.setProduct(product);
                fileRepository.save(file);
            }
        }
        return ProductConverter.toProductCreateResponseDto(product);
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
