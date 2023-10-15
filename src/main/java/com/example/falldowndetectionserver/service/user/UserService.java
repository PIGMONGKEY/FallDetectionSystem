package com.example.falldowndetectionserver.service.user;

import com.example.falldowndetectionserver.domain.dto.user.SignUpRequestDTO;
import com.example.falldowndetectionserver.domain.dto.BasicResponseDTO;
import com.example.falldowndetectionserver.domain.dto.user.UserRequestDTO;
import com.example.falldowndetectionserver.domain.dto.user.UserResponseDTO;

public interface UserService {
    public BasicResponseDTO checkCameraId(String cameraId);
    public BasicResponseDTO signup(SignUpRequestDTO signUpRequestDTO);
    public UserResponseDTO getUserInfo(String cameraId);
    public BasicResponseDTO removeUserInfo(String cameraId);
    public BasicResponseDTO modifyUserInfo(UserRequestDTO userRequestDTO);
}
