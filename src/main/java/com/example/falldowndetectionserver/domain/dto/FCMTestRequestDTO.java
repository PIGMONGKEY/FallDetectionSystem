package com.example.falldowndetectionserver.domain.dto;

import lombok.Builder;
import lombok.Data;

/**
 * FCM 발송을 테스트 하기 위한 DTO
 */
@Builder
@Data
public class FCMTestRequestDTO {
    private final String token;
    private final String title;
    private final String body;
}
