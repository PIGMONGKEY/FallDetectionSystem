package com.example.falldowndetectionserver.domain.dto.user;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class CheckCameraIdResponse {
    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}
