package com.example.falldowndetectionserver.service;

import com.example.falldowndetectionserver.domain.dto.UserDTO;

public interface UserService {
    public String signup(UserDTO userDTO);
    public UserDTO getUserInfo(String cameraId);
    public int removeUserInfo(String cameraId);
    public int modifyUserInfo(UserDTO userDTO);
}
