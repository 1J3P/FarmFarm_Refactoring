package com.example.farmfarm_refact.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name="order_detail")
@Getter
@Setter
public class OrderDetailEntity {

    @Id
    @GeneratedValue
    @Column(name="od_id")
    private Long odId;

    private int quantity;

    private long price;

    private int type;

    @ManyToOne
    @JoinColumn(name="p_id")
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name="o_id")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name="au_id")
    private AuctionEntity auction;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupEntity group;



    public OrderDetailEntity(int quantity, long price, int type, ProductEntity product, AuctionEntity auction) {
        this.quantity = quantity;
        this.price = price;
        this.type = type;
        this.product = product;
        this.auction = auction;
    }
}


