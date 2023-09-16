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
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class VideoWebSocketHandler extends TextWebSocketHandler {
    private final JSONParser jsonParser = new JSONParser();
    private final HashMap<String, String> senderSessions = new HashMap<>();
    private final HashMap<String, String> receiverSessions = new HashMap<>();
    private final HashMap<String, WebSocketSession> sessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        session.setBinaryMessageSizeLimit(1000000);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            JSONObject object = (JSONObject) jsonParser.parse(message.getPayload());
            String identifier = object.get("identifier").toString();
            //        보내는 쪽일 때 - senderSessions에 put
            if (identifier.equals("sender")) {
                senderSessions.put(object.get("camera_id").toString(), session.getId());
                sessions.put(session.getId(), session);
                log.info("sender connected");

                //        받는 쪽일 때 - receiverSessions에 put
            } else if (identifier.equals("receiver")) {
                receiverSessions.put(object.get("camera_id").toString(), session.getId());
                sessions.put(session.getId(), session);
                log.info("receiver connected");

                //        이상한 놈일 때 - 연결 종료
            } else {
                session.close();
            }
        } catch (Exception e) {
            session.close();
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        try {
            String caid = session.getHandshakeHeaders().get("camera_id").get(0);
            WebSocketSession temp_session = sessions.get(receiverSessions.get(caid));
            temp_session.sendMessage(message);
        } catch (IOException e) {
//
        } catch (NullPointerException e) {
//
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("disconnected");
    }
}
