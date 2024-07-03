package com.example.farmfarm_refact.repository;


import com.example.farmfarm_refact.dto.FarmResponseDto;
import com.example.farmfarm_refact.entity.FarmEntity;
import com.example.farmfarm_refact.entity.ProductEntity;
import com.example.farmfarm_refact.entity.UserEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<ProductEntity, Integer> {
    public List<ProductEntity> findAllByFarmAndStatusLike(FarmEntity farm, String status);
    public Optional<ProductEntity> findBypIdAndStatusLike(Long pId, String status);
    // 최신순
    public List<ProductEntity> findAllByStatusLike(Sort sort, String status);
    // 높은 가격순
    public List<ProductEntity> findAllByStatusLikeOrderByPriceDesc(String status);
    // 낮은 가격순
    public List<ProductEntity> findAllByStatusLikeOrderByPriceAsc(String status);
    // 별점순
    public List<ProductEntity> findAllByStatusLikeOrderByRatingDesc(String status);
    // 키워드 포함 정렬
    public List<ProductEntity> findAllByNameContainingAndStatusLike(@RequestParam("name") String keyword, Sort sort, String status);

}
