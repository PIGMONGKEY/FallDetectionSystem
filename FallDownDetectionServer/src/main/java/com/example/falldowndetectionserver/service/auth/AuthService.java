package com.example.falldowndetectionserver.service.auth;

import com.example.falldowndetectionserver.domain.dto.BasicResponseDTO;
import com.example.falldowndetectionserver.domain.dto.user.LoginRequestDTO;
import com.example.falldowndetectionserver.domain.dto.auth.AuthTokenParam;

public interface AuthService {
    public BasicResponseDTO<AuthTokenParam> login(LoginRequestDTO loginRequestDTO);
    public void logout(AuthTokenParam authTokenParam);
}
