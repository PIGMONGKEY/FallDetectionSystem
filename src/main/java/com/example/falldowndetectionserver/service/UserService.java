package com.example.falldowndetectionserver.service;

import com.example.falldowndetectionserver.domain.dto.LoginDTO;
import com.example.falldowndetectionserver.domain.dto.TokenDTO;
import com.example.falldowndetectionserver.domain.dto.UserDTO;

public interface UserService {
    public String signup(UserDTO userDTO);
    public TokenDTO login(LoginDTO loginDTO);
    public void logout(TokenDTO tokenDTO);
    public UserDTO getUserInfo(String cameraId);
    public String removeUserInfo(String cameraId);
    public String modifyUserInfo(UserDTO userDTO);
}
