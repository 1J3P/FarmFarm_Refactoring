package com.example.farmfarm_refact.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="user")
@NoArgsConstructor
@Getter
@Setter
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="u_id")
    private Long uId;

    //카카오에서 주는 id
    private Long id;

    private String nickname;

    private String email;

    @CreationTimestamp
    private Timestamp create_time;

    private String user_role;

    //혹시나 일반 로그인이나 naver 로그인 등 다른 로그인 수단 도입시 필요
    private String platform;

    private String status;

    private String image;

    private String refreshToken;

    private LocalDateTime refreshTokenExpiresAt;

    @OneToOne(mappedBy = "user")
    private FarmEntity farm;

    @Builder
    public UserEntity(Long id, String nickname, String email, String user_role, String platform, String status, String image) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.user_role = user_role;
        this.platform = platform;
        this.image = image;
        this.status = status;
    }
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresAt = LocalDateTime.now().plusDays(7);
    }

    // 로그아웃 시 토큰 만료
    public void refreshTokenExpires() {
        this.refreshToken = "";
        this.refreshTokenExpiresAt = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
