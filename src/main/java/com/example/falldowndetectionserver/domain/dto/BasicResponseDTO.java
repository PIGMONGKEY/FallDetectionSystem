package com.example.falldowndetectionserver.domain.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * HTTP 요청에 대한 응답 DTO
 * HTTP Status Code, HTTP Status, Message, 응답 데이터로 구성된다.
 * @param <T>
 */
@Data
@Builder
public class BasicResponseDTO<T> {
    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
    private final T data;
}
