package com.example.falldowndetectionserver.domain.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class BasicResponseDTO<T> {
    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
    private final T data;
}
