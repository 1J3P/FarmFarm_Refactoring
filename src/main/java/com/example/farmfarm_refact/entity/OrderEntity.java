package com.example.farmfarm_refact.entity;

import com.example.farmfarm_refact.entity.kakaoPay.ApprovePaymentEntity;
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
@Table(name="order_table")
@Getter
@Setter
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="o_id")
    private Long oId;

    private long totalPrice;

    private int totalQuantity;

    //결제에 대한 상태
    private PaymentStatus paymentStatus;

    //배송에 대한 상태
    private ShippingStatus shippingStatus;

    //송장 번호
    private String invoiceNumber;

    @Column(name="order_number", unique = true)
    private String orderNumber;

    @Column(name="is_delivery")
    private boolean delivery;

    private String deliveryAddress;

    private String deliveryAddressDetail;

    private String deliveryName;

    private String deliveryPhone;

    private String deliveryMemo;

    @CreationTimestamp
    private Timestamp created_at;

    @OneToMany(mappedBy = "order")
    private List<OrderDetailEntity> orderDetails = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="u_id")
    private UserEntity user;

    //왜필요할지 생각해보기
    //private int type; //0 일반, 1 공동, 2 경매

    @OneToOne
    @JoinColumn(name="pa_id")
    private ApprovePaymentEntity payment;

    @Builder
    public OrderEntity(boolean delivery, String deliveryAddress, String deliveryAddressDetail, String deliveryName, String deliveryPhone, String deliveryMemo) {
        this.delivery = delivery;
        this.deliveryAddress = deliveryAddress;
        this.deliveryAddressDetail = deliveryAddressDetail;
        this.deliveryName = deliveryName;
        this.deliveryPhone = deliveryPhone;
        this.deliveryMemo = deliveryMemo;
    }

    // 주문번호 생성 메서드
    public void generateOrderNumber() {
        this.orderNumber = String.format("%s_%d", new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date()), this.oId);
    }
}


