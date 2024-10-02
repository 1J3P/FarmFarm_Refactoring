package com.example.farmfarm_refact.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@DynamicUpdate
@Table(name="enquiry")
@Getter
@Setter
public class EnquiryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="e_id")
    private Long eId;

    private String content;

//    @Column(name="is_secret")
//    private boolean secret;

    @CreationTimestamp
    private Timestamp created_at;

    @ManyToOne
    @JoinColumn(name="product")
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name="user")
    private UserEntity user;

    private String status;

    @Builder
    public EnquiryEntity(String content, UserEntity user, String status) {
        this.content = content;
        this.user = user;
        this.status = status;
    }

    public void updateEnquiry(EnquiryEntity enquiry) {
        this.content = enquiry.getContent();
    }

}