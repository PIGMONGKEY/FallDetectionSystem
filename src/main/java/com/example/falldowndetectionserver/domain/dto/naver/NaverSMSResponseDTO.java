package com.example.falldowndetectionserver.domain.dto.naver;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NaverSMSResponseDTO {
    private final String requestId;
    private final String requestTime;
    private final String statusCode;
    private final String statusName;
}
