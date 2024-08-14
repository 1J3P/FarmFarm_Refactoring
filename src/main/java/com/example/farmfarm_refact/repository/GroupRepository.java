package com.example.farmfarm_refact.repository;

import com.example.farmfarm_refact.entity.GroupEntity;
import com.example.farmfarm_refact.entity.ProductEntity;
import com.example.farmfarm_refact.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends CrudRepository<GroupEntity, Integer> {
    public GroupEntity findBygId(Long gId);
    public List<GroupEntity> findAllByIsClose(int close);

    public List<GroupEntity> findAllByUser1(UserEntity user);

    public List<GroupEntity> findAllByUser2(UserEntity user);

    public List<GroupEntity> findAllByProduct(Optional<ProductEntity> product);
}

