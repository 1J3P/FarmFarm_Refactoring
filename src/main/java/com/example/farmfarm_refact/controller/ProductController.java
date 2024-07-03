package com.example.farmfarm_refact.controller;


import com.example.farmfarm_refact.apiPayload.ApiResponse;
import com.example.farmfarm_refact.apiPayload.code.status.SuccessStatus;
import com.example.farmfarm_refact.dto.FarmRequestDto;
import com.example.farmfarm_refact.dto.FarmResponseDto;
import com.example.farmfarm_refact.dto.ProductRequestDto;
import com.example.farmfarm_refact.dto.ProductResponseDto;
import com.example.farmfarm_refact.entity.UserEntity;
import com.example.farmfarm_refact.service.FarmService;
import com.example.farmfarm_refact.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("")
    public ApiResponse<ProductResponseDto.ProductCreateResponseDto> createProduct(@AuthenticationPrincipal UserEntity user, @RequestBody ProductRequestDto.ProductCreateRequestDto product) {
        return ApiResponse.onSuccess(productService.saveProduct(user, product));
    }

    // 상품 조회
    @GetMapping("/{pId}")
    public ApiResponse<ProductResponseDto.ProductReadResponseDto> getProduct(@PathVariable("pId") long pId) {
        return ApiResponse.onSuccess(productService.getProduct(pId));
    }

    // 상품 리스트 조회, 검색, 정렬(신상품순-default, 인기순, 낮은 가격순, 높은 가격순)
    @GetMapping("/list")
    public ApiResponse<ProductResponseDto.ProductListResponseDto> getProductList(@RequestParam(required = false, value = "sort", defaultValue = "") String criteria,
                                                                        @RequestParam(required = false, value = "keyword", defaultValue = "") String keyword) {
        if (keyword.equals("")) {
            return ApiResponse.onSuccess(productService.getProductsOrderBy(criteria));
        }
        else {
            return ApiResponse.onSuccess(productService.searchSortProducts(keyword, criteria));
        }
    }

    // 상품 삭제
    @DeleteMapping("/{pId}")
    public ApiResponse deleteProduct(@AuthenticationPrincipal UserEntity user, @PathVariable("pId") Long pId) {
        productService.deleteProduct(user, pId);
        return ApiResponse.onSuccess(SuccessStatus.LIMJANG_DELETE);
    }

    // 상품 수정
    @PatchMapping("/{pId}")
    public ApiResponse updateProduct(@PathVariable Long pId, @RequestBody @Valid ProductRequestDto.ProductUpdateRequestDto uProduct) {
        uProduct.setPId(pId);
        productService.updateProduct(uProduct);
        return ApiResponse.onSuccess(SuccessStatus.LIMJANG_UPDATE);
    }
}
