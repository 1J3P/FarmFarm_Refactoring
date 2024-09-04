package com.example.farmfarm_refact.repository;

import com.example.farmfarm_refact.entity.AuctionEntity;
import com.example.farmfarm_refact.entity.GroupEntity;
import com.example.farmfarm_refact.entity.ProductEntity;
import com.example.farmfarm_refact.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AuctionRepository extends CrudRepository<AuctionEntity, Integer> {
    public List<AuctionEntity> findAllByUser(UserEntity user);
    public List<AuctionEntity> findAllByProductOrderByPriceDescQuantityDesc(ProductEntity product);
}

