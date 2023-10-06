package com.example.falldowndetectionserver.domain.vo;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Builder
public class UPTokenVO {
    private String cameraId;
    private String token;
}
