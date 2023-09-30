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

import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class VideoWebSocketHandler extends TextWebSocketHandler {
    private final JSONParser jsonParser = new JSONParser();

//    카메라ID, 세션(receiver)
    private final HashMap<String, WebSocketSession> receiverSessions = new HashMap<>();

//    세션ID(sender), 카메라ID
    private final HashMap<String, String> senderCameraIDs = new HashMap<>();

//    연결 되었을 때
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        소켓 통신 사이즈 제한 설정
        session.setBinaryMessageSizeLimit(1000000);
    }

//    Text 메시지를 받았을 때
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            JSONObject object = (JSONObject) jsonParser.parse(message.getPayload());
            String identifier = object.get("identifier").toString();
            String cameraId = object.get("camera_id").toString();
            String sessionId = session.getId();

            //        보내는 쪽일 때 - senderSessions에 put
            if (identifier.equals("sender")) {
                senderCameraIDs.put(sessionId, cameraId);
                log.info("sender connected - " + cameraId);

            //        받는 쪽일 때 - receiverSessions에 put
            } else if (identifier.equals("receiver")) {
                receiverSessions.put(cameraId, session);
                log.info("receiver connected - " + cameraId);

            //        이상한 놈일 때 - 연결 종료
            //        TextMessage로 JSON이 아닌 메시지를 보내면 연결이 끊기게 되어 있음
            } else {
                session.close();
                log.info("stranger");
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.close();
        }
    }

//    Binary 메시지를 받았을 때
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        try {
            String cameraId = session.getHandshakeHeaders().get("camera_id").get(0);
            if (receiverSessions.containsKey(cameraId)) {
                receiverSessions
                        .get(cameraId)
                        .sendMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    연결이 끊겼을 때
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if (senderCameraIDs.getOrDefault(session.getId(), "none").equals("none")) {
            for (Map.Entry<String, WebSocketSession> hash : receiverSessions.entrySet()) {
                if (hash.getValue().equals(session)) {
                    receiverSessions.remove(hash.getKey());
                    log.info("receiver disconnected - " + hash.getKey());
                    return;
                }
            }
        } else {
            log.info("sender disconnected - " + senderCameraIDs.get(session.getId()));
            senderCameraIDs.remove(session.getId());
        }
    }
}
