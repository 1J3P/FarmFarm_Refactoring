package com.example.farmfarm_refact.config.jwt;

import com.example.farmfarm_refact.apiPayload.ExceptionHandler;
import com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    private static final int BAD_REQUEST_STATUS = HttpServletResponse.SC_BAD_REQUEST;
    private static final int UNAUTHORIZED_STATUS = 419;

    private ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("exception filter");
            filterChain.doFilter(request, response); // 한 번만 호출
            log.info("jwt success");
        } catch (NullPointerException e) {
            handleException(response, request, ErrorStatus.TOKEN_EMPTY, BAD_REQUEST_STATUS);
        } catch (ExpiredJwtException e) {
            handleException(response, request, ErrorStatus.TOKEN_UNAUTHORIZED, UNAUTHORIZED_STATUS);
        }
    }

    private void handleException(HttpServletResponse response, HttpServletRequest request, ErrorStatus errorStatus, int statusCode) throws IOException {
        Map<String, Object> body = new HashMap<>();
        ObjectMapper mapper = getObjectMapper();

        response.setStatus(statusCode);
        response.setContentType("application/json");
        body.put("timestamp", LocalDateTime.now());
        body.put("code", errorStatus.getCode());
        body.put("error", "Unauthorized");
        body.put("message", errorStatus.getMessage());
        body.put("path", request.getRequestURI());

        mapper.writeValue(response.getOutputStream(), body);
        log.error("JWT 처리 중 오류 - {}", errorStatus.getMessage());
    }
}
