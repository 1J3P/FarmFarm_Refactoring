package com.example.farmfarm_refact.repository;

import com.example.farmfarm_refact.entity.ProductEntity;
import com.example.farmfarm_refact.entity.ReviewEntity;
import com.example.farmfarm_refact.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<ReviewEntity, Long> {
    public List<ReviewEntity> findAllByUserAndStatusNotLike(UserEntity user, String status);
    public List<ReviewEntity> findAllBypIdAndStatusNotLike(Long pId, String status);
}
