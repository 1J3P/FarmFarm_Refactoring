package com.example.farmfarm_refact.service;

import com.example.farmfarm_refact.converter.ReviewConverter;
import com.example.farmfarm_refact.dto.EnquiryRequestDto;
import com.example.farmfarm_refact.dto.EnquiryResponseDto;
import com.example.farmfarm_refact.dto.ReviewRequestDto;
import com.example.farmfarm_refact.dto.ReviewResponseDto;
import com.example.farmfarm_refact.entity.OrderDetailEntity;
import com.example.farmfarm_refact.entity.ReviewEntity;
import com.example.farmfarm_refact.entity.UserEntity;
import com.example.farmfarm_refact.repository.OrderDetailRepository;
import com.example.farmfarm_refact.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public ReviewResponseDto.ReviewCreateResponseDto saveReview(UserEntity user, Long odId, ReviewRequestDto.ReviewCreateRequestDto reviewDto) {
        ReviewEntity newReview = new ReviewEntity(reviewDto.getProductStar(), reviewDto.getFarmStar(), reviewDto.getComment(), user);
        OrderDetailEntity orderDetail = orderDetailRepository.findByOdId(odId);
        newReview.setOrderDetail(orderDetail);
        ReviewEntity review = reviewRepository.save(newReview);
        return ReviewConverter.toReviewCreateResponseDto(review);
    }
}
