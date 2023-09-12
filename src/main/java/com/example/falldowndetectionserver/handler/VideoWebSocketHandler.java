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
        log.info(message.getPayload().toString());
        if (session == sessions.get(senderSessionID)) {
            for (String key : receiverSessionID) {
                WebSocketSession wss = sessions.get(key);
                try {
                    log.info("send to : " + key);
                    wss.sendMessage(new BinaryMessage(message.getPayload()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        log.info(message.getPayload());
        if (session == sessions.get(senderSessionID)) {
            for (String key : receiverSessionID) {
                WebSocketSession wss = sessions.get(key);
                try {
                    log.info("send to : " + key);
                    wss.sendMessage(new TextMessage(message.getPayload()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            session.setBinaryMessageSizeLimit(10000000);
            session.setTextMessageSizeLimit(10000000);
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
            log.info("sender disconnected");
        } else {
            receiverSessionID.remove(session.getId());
            log.info("receiver disconnected : " + session.getId());
        }
    }
}
