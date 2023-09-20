package com.example.falldowndetectionserver.service;

import com.example.falldowndetectionserver.domain.UserVO;

public interface UserService {
    public void register(UserVO userVO);
    public UserVO getUserInfo(int uno);
    public void removeUserInfo(int uno);
    public void modifyUserInfo(UserVO userVO);
}
