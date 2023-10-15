package com.example.falldowndetectionserver.service.user;

import com.example.falldowndetectionserver.domain.dto.SignUpDTO;
import com.example.falldowndetectionserver.domain.dto.UserDTO;
import com.example.falldowndetectionserver.domain.dto.user.CheckCameraIdResponse;

public interface UserService {
    public CheckCameraIdResponse checkCameraId(String cameraId);
    public String signup(SignUpDTO signUpDTO);
    public UserDTO getUserInfo(String cameraId);
    public String removeUserInfo(String cameraId);
    public String modifyUserInfo(UserDTO userDTO);
}
