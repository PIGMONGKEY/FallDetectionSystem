package com.example.falldowndetectionserver.service.auth;

import com.example.falldowndetectionserver.domain.dto.LoginDTO;
import com.example.falldowndetectionserver.domain.dto.AuthTokenDTO;

public interface AuthService {
    public AuthTokenDTO login(LoginDTO loginDTO);
    public void logout(AuthTokenDTO authTokenDTO);
}
