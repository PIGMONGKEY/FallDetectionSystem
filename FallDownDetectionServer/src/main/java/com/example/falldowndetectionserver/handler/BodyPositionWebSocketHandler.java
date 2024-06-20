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
import java.util.LinkedList;

@Slf4j
@Component
@RequiredArgsConstructor
public class BodyPositionWebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final FallDownDetector fallDownDetector;

    // <sessionId / cameraId>
    private final HashMap<String, String> sessions = new HashMap<>();

    /**
     * 웹소켓에 연결 된 후 바로 호출되는 메소드
     * session id를 모으는 HashMap에 카메라 아이디와 함께 저장하고,
     * camera id를 파라미터로 전달하여 새로운 detector를 생성하여 hashmap에 저장한다.
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        // HandshakeHeaders에서 camera_id를 받아옴
        String cameraId = session.getHandshakeHeaders().get("camera_id").get(0).toString();
        // 세션 정보 저장
        sessions.put(sessionId, cameraId);

        // 넘어짐 감지 Detector에 삽입
        fallDownDetector.getPositionHash().put(cameraId, new LinkedList<>());
        fallDownDetector.getFallDownFlagHash().put(cameraId, false);
        fallDownDetector.getEmergencyFlagHash().put(cameraId, false);

        log.info(cameraId + "'s detector connected");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // socket으로 온 message payload를 PositionVO에 매핑
        PositionVO positionVO = objectMapper.readValue(message.getPayload(), PositionVO.class);

        // 넘어짐 감지
        fallDownDetector.checkFallDown(sessions.get(session.getId()), positionVO);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info(sessions.get(session.getId()) + "'s detector disconnected");

        // Detector에서 삭제
        fallDownDetector.getPositionHash().remove(sessions.get(session.getId()));
        fallDownDetector.getFallDownTimeHash().remove(sessions.get(session.getId()));
        fallDownDetector.getEmergencyFlagHash().remove(sessions.get(session.getId()));

        // 세션 정보 삭제
        sessions.remove(session.getId());
    }
}
