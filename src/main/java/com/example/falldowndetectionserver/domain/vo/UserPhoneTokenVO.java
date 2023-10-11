package com.example.falldowndetectionserver.domain.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPhoneTokenVO {
    private String cameraId;
    private String token;
}
