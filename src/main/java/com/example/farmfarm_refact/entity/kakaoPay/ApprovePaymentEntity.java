package com.example.farmfarm_refact.entity.kakaoPay;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@DynamicUpdate
@Table(name="payment")
@Getter
@Setter
public class ApprovePaymentEntity {

    @Id
    @GeneratedValue
    @Column(name="pa_id")
    private Long paId;

    @Column(name="a_id")
    private String aid;

    @Column(name="t_id")
    private String tid;

    @Column(name="c_id")
    private String cid;

    private  String partner_order_id;

    private  String partner_user_id;

    private  String payment_method_type;

    @Embedded
    private Amount amount;

    private  String item_name;
    private  String item_code;

    private int quantity;

    private Timestamp created_at;
    private Timestamp approved_at;

    private String payload;


    @NoArgsConstructor
    @Getter
    @Setter
    @Embeddable
    public static class Amount {
        private int total;
        private int tax_free;
        private int tax;
        private int point;
        private int discount;
    }

}
