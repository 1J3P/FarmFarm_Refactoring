package com.example.farmfarm_refact.repository;


import com.example.farmfarm_refact.entity.OrderDetailEntity;
import com.example.farmfarm_refact.entity.kakaoPay.ApprovePaymentEntity;
import org.springframework.data.repository.CrudRepository;

public interface ApprovePaymentRepository extends CrudRepository<ApprovePaymentEntity, Long> {
    public ApprovePaymentEntity findBypaId(Long paId);
}
