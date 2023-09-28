package com.example.falldowndetectionserver.service;

import com.example.falldowndetectionserver.domain.UserDTO;
import com.example.falldowndetectionserver.domain.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
public class ServiceTests {
    @Autowired
    private UserService userService;

    @Test
    public void registerTest() {
        UserDTO userDTO = new UserDTO();
        List<String> phones = new ArrayList<>();
        phones.add("010-1234-1234");
        phones.add("010-2345-2345");

        userDTO.setUserName("이원희");
        userDTO.setUserPassword("Dnjsgml0202?");
        userDTO.setUserAddress("경기도 의정부시 호원동 현대아이파크 201-1201");
        userDTO.setUserPhone("010-5729-2701");
        userDTO.setCameraId("cam03");
        userDTO.setNokPhones(phones);

        userService.registerUserInfo(userDTO);
    }

    @Test
    public void getUserInfoTest() {
        log.info(userService.getUserInfo(4).toString());
    }

    @Test
    public void removeUserInfoTest() {
        userService.removeUserInfo(3);
    }

    @Test
    public void modifyUserInfoTest() {
        List<String> phones = new ArrayList<>();
        phones.add("010-6-12345436543");
        phones.add("010-2345-234654334565");

        UserDTO userDTO = userService.getUserInfo(8);
        userDTO.setUserPassword("Vkfkdshfkd12?");
        userDTO.setUserAddress("경기도 의정부시 호원동 현대아이파크 201-1201");
        userDTO.setUserPhone("010-5729-2701");
        userDTO.setNokPhones(phones);

        userService.modifyUserInfo(userDTO);
    }
}
