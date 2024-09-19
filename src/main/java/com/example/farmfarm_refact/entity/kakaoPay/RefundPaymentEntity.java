package com.example.farmfarm_refact.entity.kakaoPay;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@DynamicUpdate
@Table(name="refund_payment")
@Getter
@Setter
public class RefundPaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="re_id")
    private Long reId;

    @Column(name="a_id")
    private String aid;

    @Column(name="t_id")
    private String tid;

    @Column(name="c_id")
    private String cid;

    private String status;

    private  String partner_order_id;

    private  String partner_user_id;

    private  String payment_method_type;

    private Amount amount;

    @Embedded
    private ApprovedCancelAmount approved_cancel_amount; // 이번 요청으로 취소된 금액
    @Embedded
    private CanceledAmount canceled_amount; // 누계 취소 금액
    @Embedded
    private CancelAvailableAmount cancel_available_amount; // 남은 취소 금액

    private  String item_name;
    private  String item_code;

    private int quantity;

    private Timestamp created_at;
    private Timestamp approved_at;
    private Timestamp canceled_at;
    private String payload;

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @Embeddable
    @AttributeOverrides({
            @AttributeOverride(name = "total", column = @Column(name = "ac_total")),
            @AttributeOverride(name = "tax_free", column = @Column(name = "ac_taxfree")),
            @AttributeOverride(name = "vat", column = @Column(name = "ac_vat")),
            @AttributeOverride(name = "point", column = @Column(name = "ac_point")),
            @AttributeOverride(name = "discount", column = @Column(name = "ac_discount")),
    })
    public static class ApprovedCancelAmount {

        private int total; // 이번 요청으로 취소된 전체 금액
        private int tax_free; // 이번 요청으로 취소된 비과세 금액
        private int vat; // 이번 요청으로 취소된 부가세 금액
        private int point; // 이번 요청으로 취소된 포인트 금액
        private int discount; // 이번 요청으로 취소된 할인 금액

    }

    /**
     * 누계 취소 금액
     */
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @Embeddable
    @AttributeOverrides({
            @AttributeOverride(name = "total", column = @Column(name = "c_total")),
            @AttributeOverride(name = "tax_free", column = @Column(name = "c_taxfree")),
            @AttributeOverride(name = "vat", column = @Column(name = "c_vat")),
            @AttributeOverride(name = "point", column = @Column(name = "c_point")),
            @AttributeOverride(name = "discount", column = @Column(name = "c_discount")),
    })
    public static class CanceledAmount {

        private int total; // 취소된 전체 누적 금액
        private int tax_free; // 취소된 비과세 누적 금액
        private int vat; // 취소된 부가세 누적 금액
        private int point; // 취소된 포인트 누적 금액
        private int discount; // 취소된 할인 누적 금액

    }

    /**
     * 취소 요청 시 전달한 값
     */
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @Embeddable
    @AttributeOverrides({
            @AttributeOverride(name = "total", column = @Column(name = "ca_total")),
            @AttributeOverride(name = "tax_free", column = @Column(name = "ca_taxfree")),
            @AttributeOverride(name = "vat", column = @Column(name = "ca_vat")),
            @AttributeOverride(name = "point", column = @Column(name = "ca_point")),
            @AttributeOverride(name = "discount", column = @Column(name = "ca_discount")),
    })
    public static class CancelAvailableAmount {

        private int total; // 전체 취소 가능 금액
        private int tax_free; // 취소 가능 비과세 금액
        private int vat; // 취소 가능 부가세 금액
        private int point; // 취소 가능 포인트 금액
        private int discount; // 취소 가능 할인 금액

    }

    @NoArgsConstructor
    @Getter
    @Setter
    @Embeddable
    @AttributeOverrides({
            @AttributeOverride(name = "total", column = @Column(name = "a_total")),
            @AttributeOverride(name = "tax_free", column = @Column(name = "a_taxfree")),
            @AttributeOverride(name = "vat", column = @Column(name = "a_vat")),
            @AttributeOverride(name = "point", column = @Column(name = "a_point")),
            @AttributeOverride(name = "discount", column = @Column(name = "a_discount")),
    })
    public static class Amount {
        private int total;
        private int tax_free;
        private int tax;
        private int point;
        private int discount;
    }

}
