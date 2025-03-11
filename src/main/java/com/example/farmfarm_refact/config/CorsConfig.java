package com.example.farmfarm_refact.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 인증 정보 포함 허용
        config.setAllowedOrigins(List.of(
            "https://farm-farm.store",  // 프론트엔드 도메인
            "https://back.farm-farm.store" // 백엔드 도메인 (필요할 경우)
        ));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        config.setExposedHeaders(List.of("Authorization")); // 필요한 경우
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); // 모든 HTTP 메서드 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
