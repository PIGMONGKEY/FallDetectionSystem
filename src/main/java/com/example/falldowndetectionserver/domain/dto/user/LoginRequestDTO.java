package com.example.falldowndetectionserver.domain.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequestDTO {
    private final String cameraId;
    private final String password;
    private final String deviceToken;
}
