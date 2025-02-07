package com.example.farmfarm_refact.config.jwt;

import com.example.farmfarm_refact.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtService jwtService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("JwtAuthenticationFilter 실행 - 요청 URI: {}", request.getRequestURI());

        // 헤더에서 토큰 가져오기
        String token = jwtService.resolveToken(request);

        if (StringUtils.isEmpty(token)) {
            log.warn("요청에 JWT 토큰이 없습니다 - URI: {}", request.getRequestURI());
            chain.doFilter(request, response);
            return;
        }

        // 토큰 유효성 검사
        if (!jwtService.validateTokenBoolean(token)) {
            log.warn("유효하지 않은 JWT 토큰입니다 - URI: {}", request.getRequestURI());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 Access Token입니다.");
            return;
        }

        // 토큰이 유효한 경우
        Authentication authentication = jwtService.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Security Context에 '{}' 인증 정보를 저장 - URI: {}", authentication.getName(), request.getRequestURI());

        chain.doFilter(request, response);
    }
}
