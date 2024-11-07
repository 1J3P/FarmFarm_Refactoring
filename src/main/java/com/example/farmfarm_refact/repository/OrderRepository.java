package com.example.farmfarm_refact.repository;


import com.example.farmfarm_refact.entity.FarmEntity;
import com.example.farmfarm_refact.entity.OrderEntity;
import com.example.farmfarm_refact.entity.UserEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    public OrderEntity findByoId(Long oId);
    public List<OrderEntity> findAllByUser(UserEntity user);


}
