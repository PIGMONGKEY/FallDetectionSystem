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
//    카메라ID, 세션ID
    private final HashMap<String, String> senderSessions = new HashMap<>();
//    카메라ID, 세션ID
    private final HashMap<String, String> receiverSessions = new HashMap<>();
//    세션ID, 세션
    private final HashMap<String, WebSocketSession> sessions = new HashMap<>();

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

            //        보내는 쪽일 때 - senderSessions에 put
            if (identifier.equals("sender")) {
                senderSessions.put(cameraId, session.getId());
                sessions.put(session.getId(), session);
                log.info("sender connected");

            //        받는 쪽일 때 - receiverSessions에 put
            } else if (identifier.equals("receiver")) {
                receiverSessions.put(cameraId, session.getId());
                sessions.put(session.getId(), session);
                log.info("receiver connected");

            //        이상한 놈일 때 - 연결 종료
            //        TextMessage로 JSON이 아닌 메시지를 보내면 연결이 끊기게 되어 있음
            } else {
                session.close();
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
                WebSocketSession temp_session = sessions.get(receiverSessions.get(cameraId));
                temp_session.sendMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    연결이 끊겼을 때
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        HashMap<세션ID, 세션> 에서 삭제
        sessions.remove(session.getId());

//        receiver 세션 모음에 있으면 삭제 후 메소드 종료
        for (Map.Entry<String, String> set : receiverSessions.entrySet()) {
            if (set.getValue().equals(session.getId())) {
                receiverSessions.remove(set.getKey());
                log.info("receiver disconnected");
                return;
            }
        }

//        sender 세션 모음에 있으면 삭제 후 메소드 종료
        for (Map.Entry<String, String> set : senderSessions.entrySet()) {
            if (set.getValue().equals(session.getId())) {
                senderSessions.remove(set.getKey());
                log.info("sender disconnected");
                return;
            }
        }
    }
}
