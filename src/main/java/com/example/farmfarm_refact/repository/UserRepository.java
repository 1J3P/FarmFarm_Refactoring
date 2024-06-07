package com.example.farmfarm_refact.repository;


import com.example.farmfarm_refact.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    public UserEntity findByEmail(String email);
    //public Optional<UserEntity> findByUId(Long uId);

    Optional<UserEntity> findByRefreshToken(String refreshToken);
}
