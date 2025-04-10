package com.example.farmfarm_refact.service;

import com.example.farmfarm_refact.apiPayload.ExceptionHandler;
import com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus;
import com.example.farmfarm_refact.converter.EnquiryConverter;
import com.example.farmfarm_refact.converter.ReviewConverter;
import com.example.farmfarm_refact.dto.EnquiryRequestDto;
import com.example.farmfarm_refact.dto.EnquiryResponseDto;
import com.example.farmfarm_refact.dto.ReviewRequestDto;
import com.example.farmfarm_refact.dto.ReviewResponseDto;
import com.example.farmfarm_refact.entity.*;
import com.example.farmfarm_refact.repository.OrderDetailRepository;
import com.example.farmfarm_refact.repository.ProductRepository;
import com.example.farmfarm_refact.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus.ENQUIRY_USER_NOT_EQUAL;
import static com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus.REVIEW_USER_NOT_EQUAL;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private FarmService farmService;


    // 리뷰 등록
    public ReviewResponseDto.ReviewCreateResponseDto saveReview(UserEntity user, Long odId, ReviewRequestDto.ReviewCreateRequestDto reviewDto) {
        ReviewEntity newReview = new ReviewEntity(
            reviewDto.getProductStar(),
            reviewDto.getFarmStar(),
            reviewDto.getComment(),
            user,
            "리뷰등록"
        );

        OrderDetailEntity orderDetail = orderDetailRepository.findByOdId(odId);
        newReview.setOrderDetail(orderDetail);
        newReview.setPId(orderDetail.getProduct().getPId());

        ReviewEntity review = reviewRepository.save(newReview);

        // ⭐️ 리뷰 저장 후 rating 갱신
        ProductEntity product = orderDetail.getProduct();
        productService.updateProductRating(product);

        FarmEntity farm = product.getFarm();
        farmService.updateFarmRating(farm);

        return ReviewConverter.toReviewCreateResponseDto(review);
    }

    // 리뷰 수정
    public void updateReview(UserEntity user, Long rId, ReviewRequestDto.ReviewUpdateRequestDto reviewUpdateRequestDto) {
        ReviewEntity oldReview = reviewRepository.findById(rId)
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.REVIEW_NOT_FOUND));
        ReviewEntity newReview = ReviewConverter.toNewReview(reviewUpdateRequestDto);
        if (user.equals(oldReview.getUser())) {
            oldReview.updateReview(newReview);
            reviewRepository.save(oldReview);
        }
        else
            throw new ExceptionHandler(REVIEW_USER_NOT_EQUAL);
    }

    // 리뷰 삭제
    public void deleteReview(UserEntity user, Long rId) {
        ReviewEntity review = reviewRepository.findById(rId)
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.REVIEW_NOT_FOUND));
        if (user.equals(review.getUser())) {
            review.setStatus("리뷰삭제");
            reviewRepository.save(review);
        }
        else
            throw new ExceptionHandler(REVIEW_USER_NOT_EQUAL);
    }

    // 상품별 리뷰 조회
    public ReviewResponseDto.ReviewListResponseDto getProductReviewList(Long pId) {
//        ProductEntity product = productRepository.findBypIdAndStatusLike(pId, "yes")
//                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.PRODUCT_NOT_FOUND));
        List<ReviewEntity> reviewList = reviewRepository.findAllBypIdAndStatusNotLike(pId, "리뷰삭제");
        return ReviewConverter.toReviewList(reviewList);
    }

    // 내가 쓴 리뷰 보기
    public ReviewResponseDto.ReviewListResponseDto getMyReviewList(UserEntity user) {
        List<ReviewEntity> reviewList = reviewRepository.findAllByUserAndStatusNotLike(user, "리뷰삭제");
        return ReviewConverter.toReviewList(reviewList);
    }
}
