package com.example.falldowndetectionserver.handler;

import com.example.falldowndetectionserver.domain.PositionJsonData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class PythonWebSocketHandler extends TextWebSocketHandler {
    private final PositionJsonData positionJsonData;
    JSONParser parser = new JSONParser();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Client Connected : " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String data = message.getPayload();
        JSONObject object = (JSONObject) parser.parse(data);
        log.info(object.get("min_x").toString());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("Client Disconnected : " + session.getId());
    }
}
