package com.example.falldowndetectionserver.domain.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class BasicResponseDTO {
    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}
