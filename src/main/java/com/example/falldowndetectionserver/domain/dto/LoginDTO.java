package com.example.falldowndetectionserver.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDTO {
    private String cameraId;
    private String password;
}
