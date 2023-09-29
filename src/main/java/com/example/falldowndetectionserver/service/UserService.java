package com.example.falldowndetectionserver.service;

import com.example.falldowndetectionserver.domain.UserDTO;
import com.example.falldowndetectionserver.domain.UserVO;

public interface UserService {
    public int registerUserInfo(UserDTO userDTO);
    public UserDTO getUserInfo(String cameraId);
    public int removeUserInfo(String cameraId);
    public int modifyUserInfo(UserDTO userDTO);
}
