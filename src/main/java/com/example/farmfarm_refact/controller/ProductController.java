package com.example.farmfarm_refact.controller;


import com.example.farmfarm_refact.apiPayload.ApiResponse;
import com.example.farmfarm_refact.apiPayload.code.status.SuccessStatus;
import com.example.farmfarm_refact.dto.*;
import com.example.farmfarm_refact.entity.Cart.Cart;
import com.example.farmfarm_refact.entity.Cart.Item;
import com.example.farmfarm_refact.entity.ProductEntity;
import com.example.farmfarm_refact.entity.UserEntity;
import com.example.farmfarm_refact.service.FarmService;
import com.example.farmfarm_refact.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    // 공동구매 상품 리스트
    @GetMapping("/group/list")
    public ApiResponse<ProductResponseDto.ProductListResponseDto> getGroupProductList(@RequestParam(required = false, value = "sort", defaultValue = "") String criteria,
                                                                                 @RequestParam(required = false, value = "keyword", defaultValue = "") String keyword) {
        if (keyword.equals("")) {
            return ApiResponse.onSuccess(productService.getGroupProductsOrderBy(criteria));
        }
        else {
            return ApiResponse.onSuccess(productService.searchSortGroupProducts(keyword, criteria));
        }
    }

    // 경매 상품 리스트
    @GetMapping("/auction/list")
    public ApiResponse<ProductResponseDto.ProductListResponseDto> getAuctionProductList(@RequestParam(required = false, value = "sort", defaultValue = "") String criteria,
                                                                                      @RequestParam(required = false, value = "keyword", defaultValue = "") String keyword) {
        if (keyword.equals("")) {
            return ApiResponse.onSuccess(productService.getAuctionProductsOrderBy(criteria));
        }
        else {
            return ApiResponse.onSuccess(productService.searchSortAuctionProducts(keyword, criteria));
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
    public ApiResponse updateProduct(@PathVariable Long pId, @RequestBody @Valid ProductRequestDto.ProductUpdateRequestDto productUpdateRequestDto) {
        productUpdateRequestDto.setPId(pId);
        productService.updateProduct(productUpdateRequestDto);
        return ApiResponse.onSuccess(SuccessStatus.LIMJANG_UPDATE);
    }

    // 장바구니(세션)에 상품 담기
    @PostMapping("/cart/{pId}")
    public ApiResponse addToCart(@AuthenticationPrincipal UserEntity user, @PathVariable("pId") long pId, @RequestBody CartRequestDto.ItemDto itemDto, HttpSession session) {
        productService.addToCart(user, pId, itemDto.getQuantity(), session);
        return ApiResponse.onSuccess(SuccessStatus.CART_ITEM_ADD);
    }

    // 장바구니로 이동해서 담은 상품 조회하기
    @GetMapping("/cart")
    public ApiResponse<CartResponseDto.ItemListResponseDto> forwardToCart(HttpSession session) {
        return ApiResponse.onSuccess(productService.getCartItemList(session));
    }

    // 장바구니에 있는 상품 삭제하기
    @DeleteMapping("/cart/delete/{pId}")
    public ApiResponse deleteFromCart(@PathVariable("pId") Long pId, HttpSession session) {
        productService.deleteCartItem(pId, session);
        return ApiResponse.onSuccess(SuccessStatus.CART_ITEM_DELETE);
    }

    // 공동구매 참여자 목록
    @PostMapping("/{pId}/groupList")
    public ApiResponse<GroupResponseDto.GroupListResponseDto> getGroupList(@PathVariable("pId") long pId) {
        return ApiResponse.onSuccess(productService.getGroupList(pId));
    }
}
