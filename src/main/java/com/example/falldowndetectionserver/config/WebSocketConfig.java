package com.example.falldowndetectionserver.config;

import com.example.falldowndetectionserver.handler.PythonWebSocketHandler;
import com.example.falldowndetectionserver.handler.VideoWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final PythonWebSocketHandler pythonWebSocketHandler;
    private final VideoWebSocketHandler videoWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(videoWebSocketHandler, "/video");
        registry.addHandler(pythonWebSocketHandler, "/python");
    }
}
