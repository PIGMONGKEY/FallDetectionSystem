package com.example.falldowndetectionserver.domain.vo;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Builder
public class NokPhoneVO {
    private String cameraId;
    private String nokPhone;
    private String token;
}
