package com.example.falldowndetectionserver.service;

import com.example.falldowndetectionserver.domain.dto.LoginDTO;
import com.example.falldowndetectionserver.domain.dto.TokenDTO;

public interface AuthService {
    public TokenDTO login(LoginDTO loginDTO);
    public void logout(TokenDTO tokenDTO);
}
