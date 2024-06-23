package com.example.farmfarm_refact.repository;


import com.example.farmfarm_refact.entity.FarmEntity;
import com.example.farmfarm_refact.entity.UserEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public interface FarmRepository extends CrudRepository<FarmEntity, Integer> {
    public Optional<FarmEntity> findByfIdAndStatusLike(Long fId, String status);
    public Optional<FarmEntity> findByUserAndStatusLike(UserEntity user, String status);
    public List<FarmEntity> findAllByStatusLike(Sort sort, String status);
    public List<FarmEntity> findAllByStatusLikeOrderByRatingDesc(String status);
    public List<FarmEntity> findAllByNameContainingAndStatusLike(@RequestParam("name") String keyword, String status);
    public List<FarmEntity> findAllByNameContainingAndStatusLike(@RequestParam("name") String keyword, Sort sort, String status);
    public List<FarmEntity> findAllByLocationCityAndLocationGu(String locationCity, String locationGu);
}
