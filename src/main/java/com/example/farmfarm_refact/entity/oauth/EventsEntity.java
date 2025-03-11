package com.example.farmfarm_refact.entity.oauth;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@DynamicUpdate
@Table(name="events")
@Getter
@Setter
public class EventsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ev_id")
    private Long evId;

    private String title;

    private String content;

    private Timestamp end_date;

    private String image_url;

    @CreationTimestamp
    private Timestamp created_at;

    @UpdateTimestamp
    private Timestamp updated_at;
}
