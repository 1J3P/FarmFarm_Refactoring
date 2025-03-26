package com.example.farmfarm_refact.handler;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;


public class MyWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // WebSocket 연결이 성공적으로 성립된 후 호출되는 메서드
        System.out.println("WebSocket 연결이 성립되었습니다: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 클라이언트로부터 메시지를 받았을 때 호출되는 메서드
        System.out.println("클라이언트로부터 받은 메시지: " + message.getPayload());

        // 클라이언트로 응답 메시지를 보냄
        try {
            String responseMessage = "서버로부터의 응답: " + message.getPayload();
            session.sendMessage(new TextMessage(responseMessage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // WebSocket 통신 중 에러 발생 시 호출되는 메서드
        System.out.println("WebSocket 통신 오류 발생: " + exception.getMessage());
        exception.printStackTrace();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // WebSocket 연결이 종료된 후 호출되는 메서드
        System.out.println("WebSocket 연결이 종료되었습니다: " + session.getId());
    }
}