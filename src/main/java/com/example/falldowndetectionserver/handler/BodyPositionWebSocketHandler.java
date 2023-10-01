package com.example.falldowndetectionserver.handler;

import com.example.falldowndetectionserver.domain.vo.PositionVO;
import com.example.falldowndetectionserver.fallDownDetect.FallDownDetector;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class BodyPositionWebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;

//    <cameraId / FallDownDetector>
    private final HashMap<String, FallDownDetector> detectorHashMap = new HashMap<>();

//    <sessionId / cameraId>
    private final HashMap<String, String> sessions = new HashMap<>();

    /**
     * 웹소켓에 연결 된 후 바로 호출되는 메소드
     * session id를 모으는 HashMap에 카메라 아이디와 함께 저장하고,
     * camera id를 파라미터로 전달하여 새로운 detector를 생성하여 hashmap에 저장한다.
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        String cameraId = session.getHandshakeHeaders().get("camera_id").get(0).toString();
        sessions.put(sessionId, cameraId);
        detectorHashMap.put(cameraId, FallDownDetector.getNewDetector(cameraId));

        log.info(cameraId + "'s detector connected");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        PositionVO positionVO = objectMapper.readValue(message.getPayload(), PositionVO.class);
        String sessionId = session.getId();
        detectorHashMap
                .get(sessions.get(sessionId))
                .pushPosition(positionVO);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info(sessions.get(session.getId()) + "'s detector disconnected");
        detectorHashMap.remove(sessions.get(session.getId()));
        sessions.remove(session.getId());
    }
}
