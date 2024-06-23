package com.example.farmfarm_refact.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@DynamicUpdate
@Table(name="product")
@Getter
@Setter
public class ProductEntity {
    @Id
    @GeneratedValue
    @Column(name = "p_id")
    private Long pId;

    private String name;

    private Double rating; // 상품 별점

    private int sales; // 주문량

    private String detail;

    @CreationTimestamp
    private Timestamp created_at;

    private String closeCalendar;

    private int quantity;

    private int auctionQuantity;

    private int price;

    ShippingMethod shippingMethod;

    private String directLocation;  // 직거래 장소

    private int lowPrice;  // 경매 최저가

    private int openStatus; //0이면 아직 안열림, 1이면 열려있음, 2이면 열렸다가 닫힘 -> 처음값은 무조건 0

    private int type;

    @ManyToOne
    @JoinColumn(name="farm")
    private FarmEntity farm;

    private ProductCategory productCategory;

    private String status;

    public ProductEntity(String name, String detail, int type, ProductCategory productCategory, ShippingMethod shippingMethod, String status) {
        this.name = name;
        this.detail = detail;
        this.type = type;
        this.productCategory = productCategory;
        this.shippingMethod = shippingMethod;
        this.status = status;
    }
}
