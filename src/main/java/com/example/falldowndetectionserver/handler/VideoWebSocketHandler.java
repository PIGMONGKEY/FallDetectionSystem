package com.example.falldowndetectionserver.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
    private final JSONParser jsonParser = new JSONParser();
    private final HashMap<String, WebSocketSession> sessions = new HashMap<>();
    private String senderSessionID;
    private List<String> receiverSessionID = new ArrayList<>();



    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        session.setBinaryMessageSizeLimit(1000000);
        try {
            if (session.getHandshakeHeaders().get("identifier").get(0).equals("sender")) {
                senderSessionID = session.getId();
                log.info("sender connected : " + session.getId());
            } else {
                receiverSessionID.add(session.getId());
                log.info("receiver connected : " + session.getId());
                log.info(session.getHandshakeHeaders().get("identifier").get(0));
            }
        } catch (Exception e) {
            receiverSessionID.add(session.getId());
            log.info("receiver connected : " + session.getId());
            log.info(session.getHandshakeHeaders().toString());
        }

        sessions.put(session.getId(), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JSONObject object = (JSONObject) jsonParser.parse(message.getPayload());

    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        if (session == sessions.get(senderSessionID)) {
            for (String key : receiverSessionID) {
                WebSocketSession wss = sessions.get(key);
                try {
                    wss.sendMessage(new BinaryMessage(message.getPayload()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
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
            log.info("receiver disconnected");
        }
    }
}
