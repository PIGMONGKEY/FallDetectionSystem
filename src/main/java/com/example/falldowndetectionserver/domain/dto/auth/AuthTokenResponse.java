package com.example.falldowndetectionserver.domain.dto.auth;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class AuthTokenResponse {
    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
    private final String token;
}
