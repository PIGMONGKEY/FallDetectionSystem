package com.example.falldowndetectionserver.domain.dto.auth;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class AuthTokenParam {
    private final String token;
}
