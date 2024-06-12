package com.example.farmfarm_refact.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;


@Data
@Entity
@NoArgsConstructor
@DynamicUpdate
@Table(name="farm")
@Getter
@Setter
@Builder
public class FarmEntity {
    @Id
    @GeneratedValue
    @Column(name="f_id")
    private Long fId;

    private String name;

    private String locationCity;

    private String locationGu;

    private String locationFull;

    private String locationDetail;

    private String detail;

    private Double rating;

    private String image;

    private int auction_time;

    @Column(name="is_auction")
    private boolean auction;

    @CreationTimestamp
    private Timestamp created_at;

    @OneToOne
    @JoinColumn(name="user")
    private UserEntity user;

    private String status;

    @Builder
    public FarmEntity(String name, String locationCity, String locationGu, String locationFull, String locationDetail, String detail, String image, int auction_time) {
        this.name = name;
        this.locationCity = locationCity;
        this.locationGu = locationGu;
        this.locationFull = locationFull;
        this.locationDetail = locationDetail;
        this.detail = detail;
        this.image = image;
        this.auction_time = auction_time;
    }



}


