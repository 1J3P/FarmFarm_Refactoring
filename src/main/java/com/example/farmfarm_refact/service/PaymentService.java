package com.example.farmfarm_refact.service;


import com.example.farmfarm_refact.entity.*;
import com.example.farmfarm_refact.entity.kakaoPay.ApprovePaymentEntity;
import com.example.farmfarm_refact.entity.kakaoPay.KakaoReadyResponse;
import com.example.farmfarm_refact.entity.kakaoPay.RefundPaymentEntity;
import com.example.farmfarm_refact.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ApprovePaymentRepository approvePaymentRepository;
    private final RefundPaymentRepository refundPaymentRepository;
//    private final OrderService orderService;
    private final OrderRepository orderRepository;

    private String cid = "TC0ONETIME";
    @Value("${kakaoAdminKey}")
    private String adminKey;
    @Value("${serverUrl}")
    private String serverUrl;

    private KakaoReadyResponse response;
    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();

        String auth = "KakaoAK " + adminKey;

        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return httpHeaders;
    }

    public KakaoReadyResponse kakaoPayReady(OrderEntity order) {

        // 카카오페이 요청 양식
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("partner_order_id", String.valueOf(order.getOId()));
        parameters.add("partner_user_id", String.valueOf(order.getUser().getUId()));
        if (order.getOrderDetails().size() > 1) {
            parameters.add("item_name", order.getOrderDetails().get(0).getProduct().getName() + " 외 " + (order.getOrderDetails().size() - 1) + "건");
        } else {
            parameters.add("item_name", order.getOrderDetails().get(0).getProduct().getName());
        }
        parameters.add("quantity", String.valueOf(order.getTotalQuantity()));
        parameters.add("total_amount", String.valueOf(order.getTotalPrice()));
        parameters.add("vat_amount", String.valueOf(Math.round(order.getTotalPrice() * 0.1)));
        parameters.add("tax_free_amount", "0");
        parameters.add("approval_url", serverUrl + "/pay/success/" + String.valueOf(order.getOId())); // 성공 시 redirect url
        parameters.add("cancel_url",  serverUrl + "/pay/cancel"); // 취소 시 redirect url
        parameters.add("fail_url",  serverUrl + "/pay/fail"); // 실패 시 redirect url
        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        response = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/ready",
                requestEntity,
                KakaoReadyResponse.class);
        return response;
    }

    public ApprovePaymentEntity approveResponse(OrderEntity order, String pgToken) {

        // 카카오 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", response.getTid());
        parameters.add("partner_order_id", String.valueOf(order.getOId()));
        parameters.add("partner_user_id", String.valueOf(order.getUser().getUId()));
        parameters.add("pg_token", pgToken);

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        ApprovePaymentEntity approveResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/approve",
                requestEntity,
                ApprovePaymentEntity.class);

        return approvePaymentRepository.save(approveResponse);
    }

    @Transactional
    public RefundPaymentEntity kakaoRefund(ApprovePaymentEntity order) {

        // 카카오페이 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", order.getTid());
        parameters.add("cancel_amount", String.valueOf(order.getAmount().getTotal()));
        parameters.add("cancel_tax_free_amount", String.valueOf(order.getAmount().getTax_free()));
//        parameters.add("cancel_vat_amount", "0");

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        RefundPaymentEntity refundResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/cancel",
                requestEntity,
                RefundPaymentEntity.class);

        return refundPaymentRepository.save(refundResponse);
    }

    @Transactional
    public ApprovePaymentEntity afterPayment(String pgToken, Long oId) {
        OrderEntity order = orderRepository.findByoId(oId);
        ApprovePaymentEntity kakaoApprove = approveResponse(order, pgToken);
        List<OrderDetailEntity> detail = order.getOrderDetails();
        for (OrderDetailEntity od : detail) {
            // orderDetail이 경매일 경우, auction에 paId 등록하기
            if (od.getType() == 2) {
                AuctionEntity auction = od.getAuction();
                auction.setPaId(kakaoApprove.getPaId());
                auctionRepository.save(auction);
            }
            else {
                int quantity = od.getProduct().getQuantity();
                int updateQuantity = quantity - od.getQuantity();
                od.getProduct().setQuantity(updateQuantity);
                int sales = od.getProduct().getSales();
                int updateSales = sales + od.getQuantity();
                od.getProduct().setSales(updateSales);
                productRepository.save(od.getProduct());
            }
        }
        order.setPayment(kakaoApprove);
        order.setPaymentStatus(PaymentStatus.PAYMENT_COMPLETED);
        orderRepository.save(order);
        return kakaoApprove;
    }

    @Transactional
    public RefundPaymentEntity refund(long paId) {
        ApprovePaymentEntity approve = approvePaymentRepository.findBypaId(paId);
        RefundPaymentEntity kakaoCancelResponse = kakaoRefund(approve);
        OrderEntity order = orderRepository.findByoId(Long.parseLong(approve.getPartner_order_id()));
        List<OrderDetailEntity> detail = order.getOrderDetails();
        for (OrderDetailEntity od : detail) {
            int quantity = od.getProduct().getQuantity();
            int updateQuantity = quantity + od.getQuantity();
            od.getProduct().setQuantity(updateQuantity);
            int sales = od.getProduct().getSales();
            int updateSales = sales - od.getQuantity();
            od.getProduct().setSales(updateSales);
        }
        order.setPaymentStatus(PaymentStatus.PAYMENT_CANCELED);
        orderRepository.save(order);
        return kakaoCancelResponse;
    }
}
