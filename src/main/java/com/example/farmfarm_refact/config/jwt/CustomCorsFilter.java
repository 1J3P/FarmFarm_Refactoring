package com.example.farmfarm_refact.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // ✅ 최우선 실행
public class CustomCorsFilter extends GenericFilterBean {

    private static final List<String> ALLOWED_ORIGINS = List.of(
            "https://farm-farm.store",
            "http://localhost:3000"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;
        String origin = req.getHeader("Origin");

        // ✅ Docker 로그인 요청 예외 처리 (브라우저 요청이 아닌 경우 필터 적용 안 함)
        if (origin == null) {
            log.info("Docker 로그인 요청 감지: {} | CORS 필터 적용 안 함", req.getRequestURI());
            chain.doFilter(request, response);
            return;
        }

        // ✅ 요청한 Origin이 허용된 Origin인지 확인 후 CORS 설정 적용
        if (ALLOWED_ORIGINS.contains(origin)) {
            res.setHeader("Access-Control-Allow-Origin", origin);
            res.setHeader("Access-Control-Allow-Credentials", "true"); // ✅ 인증 정보 포함 허용
        }

        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type"); // ✅ 불필요한 "credentials" 제거
        res.setHeader("Access-Control-Expose-Headers", "Authorization, Refresh-Token"); // ✅ 클라이언트가 읽을 수 있는 헤더

        log.info("Custom CORS Filter 적용됨: {} {} | Origin: {}", req.getMethod(), req.getRequestURI(), origin);

        // ✅ OPTIONS 요청 처리: 즉시 200 응답 반환
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            log.info("CORS Preflight 요청 처리: {}", req.getRequestURI());
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }
}
