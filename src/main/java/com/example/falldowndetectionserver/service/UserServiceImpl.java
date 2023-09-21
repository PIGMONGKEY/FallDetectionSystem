package com.example.falldowndetectionserver.service;

import com.example.falldowndetectionserver.dao.UserDao;
import com.example.falldowndetectionserver.domain.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Override
    public void registerUserInfo(UserVO userVO) {
        userDao.insert(userVO);
    }

    @Override
    public UserVO getUserInfo(int uno) {
        return userDao.select(uno);
    }

    @Override
    public void removeUserInfo(int uno) {
        userDao.delete(uno);
    }

    @Override
    public void modifyUserInfo(UserVO userVO) {
        userDao.update(userVO);
    }
}
