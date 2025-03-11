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
        String token = resolveToken(request);
        log.info("추출된 토큰: {}", token);

        if (StringUtils.isBlank(token)) {
            log.warn("요청에 JWT 토큰이 없습니다 - URI: {}", request.getRequestURI());
            chain.doFilter(request, response);
            return;
        }

        try {

            if (!jwtService.validateToken(token)) {
                log.warn("유효하지 않은 JWT 토큰입니다: {}", token);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 Access Token입니다.");
                return;
            }

            Authentication authentication = jwtService.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Security Context에 '{}' 인증 정보를 저장 - URI: {}", authentication.getName(), request.getRequestURI());

            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰입니다 - URI: {}", request.getRequestURI());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 Access Token입니다.");
        } catch (Exception e) {
            log.error("JWT 필터 처리 중 예외 발생 - URI: {}", request.getRequestURI(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "JWT 처리 중 서버 오류가 발생했습니다.");
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        log.info("Authorization 헤더 값: {}", bearerToken);

        if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
