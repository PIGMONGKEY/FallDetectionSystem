package com.example.falldowndetectionserver.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class TokenDTO {
    private final String token;
}
