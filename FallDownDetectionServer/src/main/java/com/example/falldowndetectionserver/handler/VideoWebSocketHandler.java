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
    // 카메라ID, 세션(receiver)
    private final HashMap<String, WebSocketSession> receiverSessions = new HashMap<>();
    // 세션ID(sender), 카메라ID
    private final HashMap<String, String> senderCameraIDs = new HashMap<>();
    // 카메라ID, 세션(sender)
    private final HashMap<String, WebSocketSession> senderSessions = new HashMap<>();
    // 카메라ID, 영상URL
    private final HashMap<String, String> streamingUrls = new HashMap<>();

//    연결 되었을 때
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        소켓 통신 사이즈 제한 늘리기
        session.setBinaryMessageSizeLimit(1000000);
    }

//    Text 메시지를 받았을 때
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            // 연결 후 받은 TextMessage로 정보 확인 및 구분
            JSONObject object = (JSONObject) jsonParser.parse(message.getPayload());
            String identifier = object.get("identifier").toString();
            String cameraId = object.get("camera_id").toString();
            String sessionId = session.getId();


            // 보내는 쪽일 때 - senderSessions에 put
            if (identifier.equals("sender")) {
                try {
                    String streamingUrl = object.get("streaming_url").toString();
                    // 카메라ID, 스트리밍 주소
                    streamingUrls.put(cameraId, streamingUrl);
                } catch (Exception e) {
                    log.info(e.getMessage());
                    session.close();
                }

                // 세션ID : 카메라ID
                senderCameraIDs.put(sessionId, cameraId);
                // 카메라ID : 세션
                senderSessions.put(cameraId, session);
                log.info("sender connected - " + cameraId);

            } else if (identifier.equals("receiver")) {
            // 받는 쪽일 때 - receiverSessions에 put
                receiverSessions.put(cameraId, session);
                log.info("receiver connected - " + cameraId);

            } else {
                session.close();
                log.info("stranger");
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.close();
        }
    }

    // Binary 메시지를 받았을 때
    // 비디오를 프레임 단위로 끊은 사진으로 전송받는다.
    // 같은 cameraId를 가진 receiver에게 전송한다.
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
            streamingUrls.remove(senderCameraIDs.get(session.getId()));
            senderSessions.remove(senderCameraIDs.get(session.getId()));
            senderCameraIDs.remove(session.getId());
        }
    }

    public void sendFalldownMessageToWaiter(String cameraId, String fallDownTime) {
        try {
            senderSessions.get(cameraId).sendMessage(new TextMessage(fallDownTime));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getStreamingURL(String cameraId) {
        return streamingUrls.get(cameraId);
    }
}
