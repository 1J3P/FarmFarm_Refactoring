package com.example.farmfarm_refact.controller;


import com.example.farmfarm_refact.apiPayload.ApiResponse;
import com.example.farmfarm_refact.dto.FarmRequestDto;
import com.example.farmfarm_refact.dto.FarmResponseDto;
import com.example.farmfarm_refact.dto.ProductRequestDto;
import com.example.farmfarm_refact.dto.ProductResponseDto;
import com.example.farmfarm_refact.entity.UserEntity;
import com.example.farmfarm_refact.service.FarmService;
import com.example.farmfarm_refact.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

}
