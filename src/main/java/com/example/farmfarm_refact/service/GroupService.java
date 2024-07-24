package com.example.farmfarm_refact.service;

import com.example.farmfarm_refact.apiPayload.ExceptionHandler;
import com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus;
import com.example.farmfarm_refact.entity.GroupEntity;
import com.example.farmfarm_refact.entity.ProductEntity;
import com.example.farmfarm_refact.entity.UserEntity;
import com.example.farmfarm_refact.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public GroupEntity createGroup(UserEntity user, ProductEntity product, int quantity) {
        if (product.getType() == 1) {
            GroupEntity group = new GroupEntity();
            group.setProduct(product);
            group.setUser1(user);
            group.setCapacity(1);
            group.setIsClose(0);
            group.setStock(product.getGroupProductQuantity() - quantity);
            int q = product.getQuantity() - product.getGroupProductQuantity();  // 잔여 상품 수량
            group.getProduct().setQuantity(q);
            return groupRepository.save(group);
        }
        else
            throw new ExceptionHandler(ErrorStatus.PRODUCT_NOT_GROUP);
    }

    public GroupEntity getGroup(long gId) {
        return groupRepository.findBygId(gId);
    }

    public GroupEntity attendGroup(long gId, UserEntity user) {
        GroupEntity group = getGroup(gId);
        group.setUser2(user);
        group.setCapacity(0);
        group.setIsClose(1);
        group.setStock();
        return groupRepository.save(group);
    }

    public List<GroupEntity> findByProduct(ProductEntity product) {
        return groupRepository.findAllByProduct(product);
    }
}
