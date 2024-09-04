package com.example.farmfarm_refact.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuctionStatus {
    AUCTION_IN_PROGRESS(1),
    AUCTION_CLOSE(2);
    private final Integer statusNum;
}
