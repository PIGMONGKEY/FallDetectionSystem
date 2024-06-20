package com.example.falldowndetectionserver.service.user;

import com.example.falldowndetectionserver.domain.dto.user.SignUpRequestDTO;
import com.example.falldowndetectionserver.domain.dto.BasicResponseDTO;
import com.example.falldowndetectionserver.domain.dto.user.UserInfoRequestDTO;

public interface UserService {
    public BasicResponseDTO checkCameraId(String cameraId);
    public BasicResponseDTO signup(SignUpRequestDTO signUpRequestDTO);
    public BasicResponseDTO<UserInfoRequestDTO> getUserInfo(String cameraId);
    public BasicResponseDTO removeUserInfo(String cameraId, String token);
    public BasicResponseDTO modifyUserInfo(UserInfoRequestDTO userInfoRequestDTO);
}
