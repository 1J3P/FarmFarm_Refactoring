package com.example.farmfarm_refact.repository;

import com.example.farmfarm_refact.entity.EnquiryEntity;
import com.example.farmfarm_refact.entity.ProductEntity;
import com.example.farmfarm_refact.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EnquiryRepository extends CrudRepository<EnquiryEntity, Long> {
    public List<EnquiryEntity> findAllByProductAndStatusNotLike(ProductEntity product, String status);
    public List<EnquiryEntity> findAllByUserAndStatusNotLike(UserEntity user, String status);
    public List<EnquiryEntity> findAll();

}
