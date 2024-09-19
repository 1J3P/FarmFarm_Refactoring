package com.example.farmfarm_refact.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@NoArgsConstructor
@DynamicUpdate
@Table(name="auction")
@Getter
@Setter
public class AuctionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="au_id")
    private Long auId;

    private int quantity;

    private int price;

    private AuctionStatus status;

    private Long paId;

    @ManyToOne
    @JoinColumn(name="product")
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name="user")
    private UserEntity user;

    public AuctionEntity(int quantity, int price, AuctionStatus status, ProductEntity product, UserEntity user) {
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.product = product;
        this.user = user;
    }
}


