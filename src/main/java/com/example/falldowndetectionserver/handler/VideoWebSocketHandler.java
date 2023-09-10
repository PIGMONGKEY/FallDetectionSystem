package com.example.falldowndetectionserver.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class VideoWebSocketHandler extends TextWebSocketHandler {
    private final HashMap<String, WebSocketSession> sessions = new HashMap<>();
    private String senderSessionID;
    private List<String> receiverSessionID;

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        log.info(message.getPayload() + "");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (sessions.isEmpty()){
            session.setBinaryMessageSizeLimit(10000000);
            sessions.put(session.getId(), session);
            senderSessionID = session.getId();
        } else {
            sessions.put(session.getId(), session);
            receiverSessionID.add(session.getId());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        if (session.getId() == senderSessionID) {
            senderSessionID = null;
        } else {
            receiverSessionID.remove(session.getId());
        }
    }
}
