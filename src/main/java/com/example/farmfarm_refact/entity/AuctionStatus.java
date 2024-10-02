package com.example.farmfarm_refact.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuctionStatus {
    AUCTION_IN_PROGRESS(1),
    AUCTION_SUCCESS(2),
    AUCTION_FAILED(3);
    private final Integer statusNum;
}
