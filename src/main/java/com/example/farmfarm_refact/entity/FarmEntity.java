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
@Table(name="farm")
@Getter
@Setter
public class FarmEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="f_id")
    private Long fId;

    private String name;

    private String locationCity;

    private String locationGu;

    private String locationFull;

    private String locationDetail;

    private String detail;

    private Double rating;

    private int auction_time;

    @Column(name="is_auction")
    private boolean auction;

    @CreationTimestamp
    private Timestamp created_at;

    @OneToOne
    @JoinColumn(name="user")
    private UserEntity user;

    private String status;

    @OneToMany(mappedBy = "farm", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<FileEntity> files = new ArrayList<>();

    @Builder
    public FarmEntity(String name, String locationCity, String locationGu, String locationFull, String locationDetail, String detail, boolean auction, int auction_time) {
        this.name = name;
        this.locationCity = locationCity;
        this.locationGu = locationGu;
        this.locationFull = locationFull;
        this.locationDetail = locationDetail;
        this.detail = detail;
        this.auction = auction;
        this.auction_time = auction_time;
    }

    public void updateFarm(FarmEntity newFarm) {
        this.name = newFarm.getName();
        this.locationCity = newFarm.getLocationCity();
        this.locationGu = newFarm.getLocationGu();
        this.locationFull = newFarm.getLocationFull();
        this.locationDetail = newFarm.getLocationDetail();
        this.detail = newFarm.getDetail();

    }

}


