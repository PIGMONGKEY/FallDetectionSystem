package com.example.falldowndetectionserver.domain;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

@Data
public class SessionVO {
    private String session_id;
    private String type;
    private WebSocketSession session;
    private String camera_id;
}
