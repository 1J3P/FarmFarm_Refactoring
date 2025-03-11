package com.example.farmfarm_refact.config;

import com.example.farmfarm_refact.config.jwt.CustomCorsFilter;
import com.example.farmfarm_refact.config.jwt.JwtAuthenticationFilter;
import com.example.farmfarm_refact.config.jwt.JwtExceptionFilter;
import com.example.farmfarm_refact.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtService jwtService;
    private final CorsConfig corsConfig;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Bean
    @Order(0)
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring()
                .requestMatchers("/swagger-ui/**", "/swagger/**", "/swagger-resources/**", "/swagger-ui.html", "/test",
                        "/configuration/ui",  "/v3/api-docs/**", "/user/login/**", "/error/**", "/favicon.ico", "/pay/success/**");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults())
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new CustomCorsFilter(), JwtAuthenticationFilter.class) // ✅ CORS 필터 추가
                .addFilter(new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtService))
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers(
                                        AntPathRequestMatcher.antMatcher("/user/login/**"),
                                        AntPathRequestMatcher.antMatcher("favicon.ico"),
                                        AntPathRequestMatcher.antMatcher("/error"),
                                        AntPathRequestMatcher.antMatcher("/product/**")
                                ).permitAll()
                                .anyRequest().authenticated()
                )
                .headers(
                        headersConfigurer ->
                                headersConfigurer
                                        .frameOptions(
                                                HeadersConfigurer.FrameOptionsConfig::sameOrigin
                                        )
                )
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);

        return http.build();
    }
}
