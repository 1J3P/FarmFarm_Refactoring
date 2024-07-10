package com.example.farmfarm_refact.entity;

import com.example.farmfarm_refact.dto.ProductRequestDto;
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

    private int type; // 0: 일반 상품, 1: 공동구매 상품, 2: 경매 상품

    private int groupProductQuantity; // 공동구매 상품 수량

    private int groupProductDiscount; // 공동구매 상품 할인율

    @ManyToOne
    @JoinColumn(name="farm")
    private FarmEntity farm;

    private ProductCategory productCategory;

    private String status;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<FileEntity> files = new ArrayList<>();

    public ProductEntity(String name, String detail, int type, ProductCategory productCategory, ShippingMethod shippingMethod, String status) {
        this.name = name;
        this.detail = detail;
        this.type = type;
        this.productCategory = productCategory;
        this.shippingMethod = shippingMethod;
        this.status = status;
    }

    @Builder
    public ProductEntity(String name, String detail, int quantity, int price, ShippingMethod shippingMethod, String directLocation, ProductCategory productCategory, List<FileEntity> files) {
        this.name = name;
        this.detail = detail;
        this.quantity = quantity;
        this.price = price;
        this.shippingMethod = shippingMethod;
        this.directLocation = directLocation;
        this.productCategory = productCategory;
        this.files = files;
    }

    public void updateProduct(ProductEntity product) {
        this.name = product.getName();
        this.detail = product.getDetail();
        this.quantity = product.getQuantity();
        this.price = product.getPrice();
        this.shippingMethod = product.getShippingMethod();
        this.directLocation = product.getDirectLocation();
        this.productCategory = product.getProductCategory();
    }
}
