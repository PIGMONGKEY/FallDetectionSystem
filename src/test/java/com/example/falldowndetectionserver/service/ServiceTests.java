package com.example.falldowndetectionserver.service;

import com.example.falldowndetectionserver.domain.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class ServiceTests {
    @Autowired
    private UserService userService;

    @Test
    public void registerTest() {
        UserVO userVO = new UserVO();
        userVO.setUserName("gmlgmlgmlg");
        userVO.setUserPassword("Dnjsgml0202?");
        userVO.setUserAddress("경기도 의정부시 호원동 현대아이파크 201-1201");
        userVO.setUserPhone("010-5729-2701");
        userVO.setCameraId("cam02");

        userService.register(userVO);
    }

    @Test
    public void getUserInfoTest() {
        userService.getUserInfo(3);
    }

    @Test
    public void removeUserInfoTest() {
        userService.removeUserInfo(5);
    }

    @Test
    public void modifyUserInfoTest() {
        UserVO userVO = userService.getUserInfo(3);
        userVO.setUserName("이원희희");
        userVO.setUserPassword("Vkfkdshfkd12?");
        userVO.setUserAddress("경기도 의정부시 호원동 현대아이파크 201-1201");
        userVO.setUserPhone("010-5729-2701");
        userVO.setCameraId("cam02");

        userService.modifyUserInfo(userVO);
    }
}
