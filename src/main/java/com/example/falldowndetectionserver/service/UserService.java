package com.example.falldowndetectionserver.service;

import com.example.falldowndetectionserver.domain.UserDTO;
import com.example.falldowndetectionserver.domain.UserVO;

public interface UserService {
    public void registerUserInfo(UserDTO userDTO);
    public UserDTO getUserInfo(String cameraId);
    public void removeUserInfo(String cameraId);
    public void modifyUserInfo(UserDTO userDTO);
}
