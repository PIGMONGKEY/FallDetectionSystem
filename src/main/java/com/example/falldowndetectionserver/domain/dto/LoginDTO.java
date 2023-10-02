package com.example.falldowndetectionserver.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDTO {
    private final String cameraId;
    private final String password;
}
