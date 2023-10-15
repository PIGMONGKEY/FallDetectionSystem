package com.example.falldowndetectionserver.domain.dto.user;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
public class UserResponseDTO {
    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
    private final UserRequestDTO userInfo;
}
