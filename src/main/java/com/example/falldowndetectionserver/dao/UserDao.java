package com.example.falldowndetectionserver.dao;

import com.example.falldowndetectionserver.domain.UserVO;
import com.example.falldowndetectionserver.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserDao {
    private final UserMapper userMapper;

    public void insert(UserVO userVO) {
        userMapper.insert(userVO);
    }

    public UserVO select(int uno) {
        return userMapper.select(uno);
    }

    public void delete(int uno) {
        userMapper.delete(uno);
    }

    public void update(UserVO userVO) {
        userMapper.update(userVO);
    }
}
