package com.example.falldowndetectionserver.config;

import com.example.falldowndetectionserver.handler.BodyPositionWebSocketHandler;
import com.example.falldowndetectionserver.handler.VideoWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 웹 소켓 설정
 * Handler를 등록하고, 접근 주소를 지정한다.
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final BodyPositionWebSocketHandler pythonWebSocketHandler;
    private final VideoWebSocketHandler videoWebSocketHandler;

//    소켓 핸들러를 등록하는 메소드
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(videoWebSocketHandler, "/video")
                .setAllowedOriginPatterns("*");
        registry.addHandler(pythonWebSocketHandler, "/position");
    }
}
