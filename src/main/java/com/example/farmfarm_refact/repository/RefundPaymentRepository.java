package com.example.farmfarm_refact.repository;


import com.example.farmfarm_refact.entity.kakaoPay.ApprovePaymentEntity;
import com.example.farmfarm_refact.entity.kakaoPay.RefundPaymentEntity;
import org.springframework.data.repository.CrudRepository;

public interface RefundPaymentRepository extends CrudRepository<RefundPaymentEntity, Long> {

}
