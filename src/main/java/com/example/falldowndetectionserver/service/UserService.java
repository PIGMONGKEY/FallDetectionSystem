package com.example.falldowndetectionserver.service;

import com.example.falldowndetectionserver.domain.dto.UserDTO;

public interface UserService {
    public String signup(UserDTO userDTO);
    public UserDTO getUserInfo(String cameraId);
    public String removeUserInfo(String cameraId);
    public String modifyUserInfo(UserDTO userDTO);
}
