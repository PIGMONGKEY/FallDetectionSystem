package com.example.falldowndetectionserver.domain.dto.user;

import lombok.Builder;
import lombok.Data;

/**
 * 로그인 요청 DTO
 */
@Data
@Builder
public class LoginRequestDTO {
    private final String cameraId;
    private final String password;
    private final String deviceToken;
}
