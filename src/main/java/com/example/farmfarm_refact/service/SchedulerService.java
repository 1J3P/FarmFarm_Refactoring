package com.example.farmfarm_refact.service;


import com.example.farmfarm_refact.apiPayload.ExceptionHandler;
import com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus;
import com.example.farmfarm_refact.controller.PaymentController;
import com.example.farmfarm_refact.controller.S3Controller;
import com.example.farmfarm_refact.converter.FarmConverter;
import com.example.farmfarm_refact.dto.FarmRequestDto;
import com.example.farmfarm_refact.dto.FarmResponseDto;
import com.example.farmfarm_refact.entity.*;
import com.example.farmfarm_refact.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus.S3_NOT_FOUND;

@Service
public class SchedulerService {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentController paymentController;

    @Autowired
    private AuctionRepository auctionRepository;

    @Scheduled(cron = "0 * * * * *")
    public void closeAuction() throws ParseException {
        List<ProductEntity> products = productRepository.findAllByStatusLikeAndType("yes", 2);
        Calendar current = Calendar.getInstance();
        for (ProductEntity product : products) {
            String date = product.getCloseCalendar();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Calendar close = Calendar.getInstance();
            close.setTime(format.parse(date));
            if (current.after(close)) {
                product.setOpenStatus(2);
                productRepository.save(product);
            }
        }
    }

    @Scheduled(cron = "0 * * * * *")
    public void selectTopAuction() throws ParseException {
        List<ProductEntity> products = productRepository.findAllByStatusLikeAndType("yes", 2);
        for (ProductEntity product : products) {
            if (product.getOpenStatus() == 2) {
                List<AuctionEntity> auctions = auctionRepository.findAllByProductOrderByPriceDescQuantityDesc(product); // 제시 가격이 비싼 순 -> 수량이 많은 순으로 정렬
                for (AuctionEntity auction : auctions) {
                    if (auction.getStatus() == AuctionStatus.AUCTION_IN_PROGRESS) {
                        if (product.getOpenStatus() == 2) {  // 낙찰이 아직 되지 않았을 경우 계속 탐색 (예시로 든 변수명임. 바꿀 필요 있음!)
                            int auctionQuantity = product.getQuantity();
                            if (auctionQuantity <= 0 || auctionQuantity < auction.getQuantity()) {
                                auction.setStatus(AuctionStatus.AUCTION_FAILED); // 해당 경매건은 취소
                                product.setOpenStatus(3); //경매 낙찰도 완료됐다고 알려주기
                                productRepository.save(product);
                                auctionRepository.save(auction);
                                break;
                            }
                            else {
                                auctionQuantity -= auction.getQuantity();
                                product.setQuantity(auctionQuantity); // 상품의 경매 수량을 조정
                                auction.setStatus(AuctionStatus.AUCTION_SUCCESS); // 해당 경매건을 채택
                                productRepository.save(product);
                                auctionRepository.save(auction);
                            }

                        }
                        else if (product.getOpenStatus() == 3) {  // 낙찰이 되었을 경우 경매 기각
                            auction.setStatus(AuctionStatus.AUCTION_FAILED); // 해당 경매건을 기각
                            auctionRepository.save(auction);
                            paymentController.refund(auction.getPaId()); // 결제 취소 처리 후 그 다음 경매건을 탐색
                        }
                    }
                }
            }
        }
    }

}
