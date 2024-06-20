package com.example.falldowndetectionserver.dao;

import com.example.falldowndetectionserver.domain.vo.UserVO;
import com.example.falldowndetectionserver.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDao {
    private final UserMapper userMapper;

    public int insert(UserVO userVO) {
        try {
            return userMapper.insert(userVO);
        } catch (Exception e) {
            return -1;
        }
    }

    public Optional<UserVO> select(String cameraId) {
        return userMapper.select(cameraId);
    }

    public int delete(String cameraId) {
        try {
            return userMapper.delete(cameraId);
        } catch (Exception e) {
            return -1;
        }
    }

    public int update(UserVO userVO) {
        try {
            return userMapper.update(userVO);
        } catch (Exception e) {
            return -1;
        }
    }
}
