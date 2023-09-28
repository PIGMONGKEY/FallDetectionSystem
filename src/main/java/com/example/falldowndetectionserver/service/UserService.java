package com.example.falldowndetectionserver.service;

import com.example.falldowndetectionserver.domain.UserDTO;
import com.example.falldowndetectionserver.domain.UserVO;

public interface UserService {
    public void registerUserInfo(UserVO userVO);
    public UserDTO getUserInfo(int uno);
    public void removeUserInfo(int uno);
    public void modifyUserInfo(UserVO userVO);
}
