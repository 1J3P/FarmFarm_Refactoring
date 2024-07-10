package com.example.farmfarm_refact.controller;


import com.example.farmfarm_refact.apiPayload.ApiResponse;
import com.example.farmfarm_refact.apiPayload.code.status.SuccessStatus;
import com.example.farmfarm_refact.dto.FarmRequestDto;
import com.example.farmfarm_refact.dto.FarmResponseDto;
import com.example.farmfarm_refact.dto.ProductRequestDto;
import com.example.farmfarm_refact.dto.ProductResponseDto;
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

    // 장바구니(세션)에 상품 담기
    @PostMapping("/{pId}/cart")
    public ApiResponse addToCart(@AuthenticationPrincipal UserEntity user, @PathVariable("pId") long pId, @RequestBody ProductRequestDto.ItemDto itemDto, HttpSession session) {
        productService.addToCart(user, pId, itemDto.getQuantity(), session);
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    // 장바구니로 이동해서 담은 상품 조회하기
    @GetMapping("/cart")
    public ModelAndView forwardToCart(HttpSession session) {
        ModelAndView mav = new ModelAndView("/home/product/shoppingCart");
        List<Item> itemList = new ArrayList<>();
        Cart cart = (Cart)session.getAttribute("cart");
        if (cart != null) {
            for (Item i : cart.getItemList()) {
                itemList.add(i);
            }
        }
        mav.addObject("itemList", itemList);
        return mav;
    }

    @DeleteMapping("/cart/delete/{p_id}")
    public ResponseEntity<Object> deleteFromCart(HttpSession session, @PathVariable("p_id") long p_id) {
        UserEntity user = (UserEntity)session.getAttribute("user");
        try {
            Cart cart = (Cart)session.getAttribute("cart");
            cart.delete(p_id);
            session.setAttribute("cart", cart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("exception");
        }
        return ResponseEntity.ok().body("장바구니 삭제 완료");
    }
}
