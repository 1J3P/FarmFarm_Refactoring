package com.example.farmfarm_refact.entity.Cart;

import com.example.farmfarm_refact.entity.ProductEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Item {
    private Long uId;
    private Integer quantity;
    private ProductEntity product;
}
