package com.example.falldowndetectionserver.domain.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FCMTestRequestDTO {
    private final String token;
    private final String title;
    private final String body;
}
