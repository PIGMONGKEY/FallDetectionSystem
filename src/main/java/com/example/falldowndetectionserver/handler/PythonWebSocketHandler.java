package com.example.falldowndetectionserver.handler;

import com.example.falldowndetectionserver.domain.PositionJsonData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.LinkedList;
import java.util.Queue;

@Slf4j
@Component
@RequiredArgsConstructor
public class PythonWebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private Queue<PositionJsonData> dtos = new LinkedList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Text Connected : " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        PositionJsonData dto = objectMapper.readValue(message.getPayload(), PositionJsonData.class);
        if (dtos.size() < 60) {
            dtos.add(dto);
        } else {
            dtos.remove();
            dtos.add(dto);
        }

        log.info(dtos.toString());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("Client Disconnected : " + session.getId());
    }
}
