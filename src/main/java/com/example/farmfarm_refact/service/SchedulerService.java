package com.example.farmfarm_refact.service;



import com.example.farmfarm_refact.controller.PaymentController;
import com.example.farmfarm_refact.entity.*;
import com.example.farmfarm_refact.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Service
public class SchedulerService {
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
            System.out.println("1111111 : product - " + product.getPId());
            String date = product.getCloseCalendar();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Calendar close = Calendar.getInstance();
            close.setTime(format.parse(date));
            if (current.after(close)) {
                product.setOpenStatus(2);
                System.out.println("open status change : product - " + product.getPId());
                productRepository.save(product);
            }
        }
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void selectTopAuction() throws ParseException {
        List<ProductEntity> products = productRepository.findAllByStatusLikeAndType("yes", 2);
        for (ProductEntity product : products) {
            System.out.println("select top auction : product - " + product.getPId());
            if (product.getOpenStatus() == 2) {
                List<AuctionEntity> auctions = auctionRepository.findAllByProductOrderByPriceDescQuantityDesc(product); // 제시 가격이 비싼 순 -> 수량이 많은 순으로 정렬
                for (AuctionEntity auction : auctions) {
                    int auctionQuantity = product.getQuantity();
                    int sales = 0;

                    if (auction.getStatus() == AuctionStatus.AUCTION_IN_PROGRESS) {
                        if (product.getOpenStatus() == 2) {  // 낙찰이 아직 되지 않았을 경우 계속 탐색 (예시로 든 변수명임. 바꿀 필요 있음!)
                            // 더이상 판매할 수 있는 수량이 없으면
                            if (auctionQuantity <= 0 || auctionQuantity < auction.getQuantity()) {
                                auction.setStatus(AuctionStatus.AUCTION_FAILED);
                                product.setOpenStatus(3); // 모든 경매가 완료된 상태로 설정
                                paymentController.refund(auction.getPaId());
                                productRepository.save(product); // 수량 업데이트
                                auctionRepository.save(auction);
                                System.out.println("fail select top auction : auction - " + auction.getAuId());
                                continue;
                            } else {
                                auctionQuantity -= auction.getQuantity(); // 남은 수량에서 현재 경매 수량을 차감
                                product.setQuantity(auctionQuantity); // 남은 수량을 제품에 반영
                                sales += auction.getQuantity();
                                product.setSales(sales);
                                productRepository.save(product); // DB에 반영
                                auction.setStatus(AuctionStatus.AUCTION_SUCCESS); // 경매 성공 처리
                                auctionRepository.save(auction);
                                System.out.println("success select top auction : auction - " + auction.getAuId());
                            }
                        }
                        if (product.getOpenStatus() == 3) {  // 낙찰이 완료되었을 경우 경매 기각
                            auction.setStatus(AuctionStatus.AUCTION_FAILED); // 해당 경매건을 기각
                            auctionRepository.save(auction);
                            paymentController.refund(auction.getPaId()); // 결제 취소 처리 후 그 다음 경매건을 탐색
                            System.out.println("fail2 select top auction : auction - " + auction.getAuId());
                        }

                    }
                    product.setQuantity(auctionQuantity);
                    product.setSales(sales);
                    productRepository.save(product);
                }
            }
        }
    }

}
