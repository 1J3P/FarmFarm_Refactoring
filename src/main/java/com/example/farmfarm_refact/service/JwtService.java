package com.example.farmfarm_refact.service;


import com.example.farmfarm_refact.config.jwt.JwtAuthenticationFilter;

import com.example.farmfarm_refact.dto.TokenDto;
import com.example.farmfarm_refact.repository.UserRepository;
import io.jsonwebtoken.*;
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
import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String JWT_SECRET;


    private final UserDetailServiceImpl userDetailService;
    private Long tokenValidTime = 1000L * 60 * 60; // 1h
    private Long refreshTokenValidTime = 1000L * 60 * 60 * 24 * 7; // 7d



    private UserRepository userRepository;

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());


    // access token 생성
    public String encodeJwtToken(TokenDto tokenDto) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("juinjang")
                .setIssuedAt(now)
                .setSubject(tokenDto.getUId().toString())
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .claim("memberId", tokenDto.getUId())
                .signWith(SignatureAlgorithm.HS256,
                        Base64.getEncoder().encodeToString(("" + JWT_SECRET).getBytes(
                                StandardCharsets.UTF_8)))
                .compact();
    }

    // refresh token 생성
    public String encodeJwtRefreshToken(Long memberId) {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setSubject(memberId.toString())
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .claim("memberId", memberId)
                .claim("roles", "USER")
                .signWith(SignatureAlgorithm.HS256,
                        Base64.getEncoder().encodeToString(("" + JWT_SECRET).getBytes(
                                StandardCharsets.UTF_8)))
                .compact();
    }

    // JWT 토큰 으로부터 memberId 추출
    public Long getMemberIdFromJwtToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(Base64.getEncoder().encodeToString(("" + JWT_SECRET).getBytes(
                            StandardCharsets.UTF_8)))
                    .parseClaimsJws(token)
                    .getBody();
            return Long.parseLong(claims.getSubject());
        } catch(Exception e) {
            throw new JwtException(e.getMessage());
        }
    }


    // Autorization : Bearer에서 token 추출 (refreshToken, accessToken 포함)
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtAuthenticationFilter.AUTHORIZATION_HEADER);
        if(bearerToken == null)
            throw new NullPointerException();
        else if(StringUtils.isNotEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 유효성 + 만료일자 확인 (만료 여부만 확인. 에러 발생 x)
    public Boolean validateTokenBoolean(String token) {
        Date now = new Date();

        try{
            // 주어진 토큰을 파싱하고 검증.
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(Base64.getEncoder().encodeToString(("" + JWT_SECRET).getBytes(StandardCharsets.UTF_8)))
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date(now.getTime()));
        }catch (JwtException e){

            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }

    // JWT 토큰 인증 정보 조회 (토큰 복호화)
    public Authentication getAuthentication(String token) {
        System.out.println(this.getMemberIdFromJwtToken(token));

        UserDetails userDetails = userDetailService.loadUserByUsername(this.getMemberIdFromJwtToken(token).toString());
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    // 토큰 유효 시간 확인
    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(accessToken).getBody().getExpiration();
        Long now = new Date().getTime();

        return (expiration.getTime() - now);
    }




    public String decodeHeader(String token) {
        return new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
    }

    public Claims getTokenClaims(String token, PublicKey publicKey) {
        log.info("getTokenClaims --------");
        //여기서 에러남
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
