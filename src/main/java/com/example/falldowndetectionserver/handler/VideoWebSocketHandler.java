package com.example.falldowndetectionserver.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class VideoWebSocketHandler extends TextWebSocketHandler {
    private final HashMap<String, WebSocketSession> sessions = new HashMap<>();
    private String senderSessionID;
    private List<String> receiverSessionID = new ArrayList<>();

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        if (session == sessions.get(senderSessionID)) {
            for (String key : sessions.keySet()) {
                WebSocketSession wss = sessions.get(key);
                try {
                    wss.sendMessage(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if (session == sessions.get(senderSessionID)) {
            for (String key : sessions.keySet()) {
                try {
                    WebSocketSession wss = sessions.get(key);
                    wss.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            session.setBinaryMessageSizeLimit(10000000);
        if (sessions.isEmpty()){
            sessions.put(session.getId(), session);
            senderSessionID = session.getId();
            log.info("sender connected : " + senderSessionID);
        } else {
            sessions.put(session.getId(), session);
            receiverSessionID.add(session.getId());
            log.info("receiver connected : " + session.getId());
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
