package com.example.farmfarm_refact.config;

import com.example.farmfarm_refact.handler.MyWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MyWebSocketHandler(), "/ws")
                .setAllowedOrigins("https://farm-farm.store") // HTTPS에서만 접속 허용
                .withSockJS(); // SockJS 지원 (선택 사항)
    }
}