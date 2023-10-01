package com.example.falldowndetectionserver.mapper;

import com.example.falldowndetectionserver.domain.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class UserMapperTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void insertTest() {
        UserVO userVO = new UserVO();
        userVO.setUserName("이원희");
        userVO.setUserPassword("Dnjsgml0202?");
        userVO.setUserAddress("경기도 의정부시 호원동 현대아이파크 201-1201");
        userVO.setUserPhone("010-5729-2701");
        userVO.setCameraId("cam01");

        userMapper.insert(userVO);
    }

    @Test
    public void selectTest() {
        log.info(userMapper.select("cam01").toString());
    }

    @Test
    public void deleteTest() {
        userMapper.delete("cam01");
    }

    @Test
    public void updateTest() {
        UserVO userVO = userMapper.select("cam01");
        userVO.setUserName("modified");
        userVO.setUserPassword("Dnjsgml0202?");
        userVO.setUserAddress("경기도 의정부시 호원동 현대아이파크 201-1201");
        userVO.setUserPhone("010-5729-2701");

        userMapper.update(userVO);
    }
}
