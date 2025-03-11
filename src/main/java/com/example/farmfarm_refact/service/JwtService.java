package com.example.farmfarm_refact.service;

import com.example.farmfarm_refact.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    private final UserDetailServiceImpl userDetailService;
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final long ACCESS_TOKEN_VALIDITY = 1000L * 60 * 15; // 15분
    private final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 7; // 7일

    // ✅ accessToken 생성 (generateAccessToken)
    public String generateAccessToken(Long userId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .signWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ refreshToken 생성 (generateRefreshToken)
    public String generateRefreshToken(Long userId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .signWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ 토큰 유효성 검사 (validateToken)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(JWT_SECRET.getBytes(StandardCharsets.UTF_8)).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT 만료됨: {}", token);
            return false;
        } catch (JwtException e) {
            log.error("유효하지 않은 JWT: {}", e.getMessage());
            return false;
        }
    }

    // ✅ 토큰에서 사용자 ID 추출 (extractUserIdFromToken)
    public Long extractUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    // ✅ JWT 토큰 인증 정보 조회 (getAuthentication)
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();

        String userId = claims.getSubject();
        UserDetails userDetails = userDetailService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }
}
