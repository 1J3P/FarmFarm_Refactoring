package com.example.farmfarm_refact.repository;


import com.example.farmfarm_refact.entity.FarmEntity;
import com.example.farmfarm_refact.entity.OrderEntity;
import com.example.farmfarm_refact.entity.UserEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends CrudRepository<OrderEntity, Long> {

    public OrderEntity findByoId(Long oId);
    public List<OrderEntity> findAllByUser(UserEntity user);

    // 내 상품을 구매한 order list
    @Query("SELECT o FROM OrderEntity o " +
            "JOIN o.orderDetails od " +
            "JOIN od.product p " +
            "JOIN p.farm f " +
            "WHERE f.user = :user")
    List<OrderEntity> findOrdersByFarmOwner(@Param("user") UserEntity user);

}
