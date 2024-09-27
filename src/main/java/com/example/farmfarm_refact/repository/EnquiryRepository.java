package com.example.farmfarm_refact.repository;

import com.example.farmfarm_refact.entity.EnquiryEntity;
import com.example.farmfarm_refact.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EnquiryRepository extends CrudRepository<EnquiryEntity, Long> {
    public List<EnquiryEntity> findAllByProductAndStatusNotLike(ProductEntity product, String status);

}
