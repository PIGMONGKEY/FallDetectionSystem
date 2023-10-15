package com.example.falldowndetectionserver.service.auth;

import com.example.falldowndetectionserver.domain.dto.LoginDTO;
import com.example.falldowndetectionserver.domain.dto.auth.AuthTokenParam;
import com.example.falldowndetectionserver.domain.dto.auth.AuthTokenResponse;

public interface AuthService {
    public AuthTokenResponse login(LoginDTO loginDTO);
    public void logout(AuthTokenParam authTokenParam);
}
