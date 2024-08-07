package com.example.farmfarm_refact.controller;


import com.example.farmfarm_refact.apiPayload.ApiResponse;
import com.example.farmfarm_refact.entity.OrderDetailEntity;
import com.example.farmfarm_refact.entity.OrderEntity;
import com.example.farmfarm_refact.entity.kakaoPay.ApprovePaymentEntity;
import com.example.farmfarm_refact.entity.kakaoPay.KakaoReadyResponse;
import com.example.farmfarm_refact.entity.kakaoPay.RefundPaymentEntity;
import com.example.farmfarm_refact.service.OrderService;
import com.example.farmfarm_refact.service.PaymentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pay")
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderService orderService;

    //장바구니 페이지에서 주문하기 눌렀을때 호출되는 API - 오더 디테일 객체들 만들어서 session에 저장해주고, 직거래만 되는지 표시
    @GetMapping("/order/{oId}")
    public ApiResponse<KakaoReadyResponse> pay(@PathVariable("oId") Long oId) {
        return ApiResponse.onSuccess(paymentService.kakaoPayReady(orderService.getOrder(oId)));
    }

    @GetMapping("/success/{oId}")
    public void afterPayRequest(HttpServletResponse response, @RequestParam("pg_token") String pgToken, @PathVariable("oId") long oId) throws IOException {
        // 결제 성공 시
        response.sendRedirect("http://localhost:3000/paymentCallback?status=success&pg_token=" + pgToken + "&oId=" + oId);
    }

    @GetMapping("/fail/{oId}")
    public void afterPayFail(HttpServletResponse response, @PathVariable("oId") long oId) throws IOException {
        // 결제 실패 시
        response.sendRedirect("http://localhost:3000/paymentCallback?status=fail&oId=" + oId);
    }

    @PostMapping("/refund/{paId}")
    public ApiResponse<RefundPaymentEntity> refund(@PathVariable("paId") long paId) {
        return ApiResponse.onSuccess(paymentService.refund(paId));
    }

}