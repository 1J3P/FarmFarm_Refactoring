package com.example.farmfarm_refact.repository;


import com.example.farmfarm_refact.entity.OrderDetailEntity;
import com.example.farmfarm_refact.entity.ProductEntity;
import com.example.farmfarm_refact.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderDetailRepository extends CrudRepository<OrderDetailEntity, Long> {
    List<OrderDetailEntity> findByOrderUserAndType(UserEntity user, Integer type);
    OrderDetailEntity findByOdId(Long oId);

    List<OrderDetailEntity> findByProduct(ProductEntity product);

}

