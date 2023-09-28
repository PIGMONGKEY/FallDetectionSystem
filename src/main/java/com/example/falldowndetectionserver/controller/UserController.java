package com.example.falldowndetectionserver.controller;

import com.example.falldowndetectionserver.domain.UserDTO;
import com.example.falldowndetectionserver.domain.UserVO;
import com.example.falldowndetectionserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/")
public class UserController {
    private final UserService userService;

    /**
     * 사용자 정보를 받아옴
     * @param cameraId PK인 UNO를 전달해준다
     * @return 결과 UserVO를 JSON 형태로 리턴한다.
     */
    @GetMapping("info")
    public UserDTO getUserInfo(String cameraId) {
        return userService.getUserInfo(cameraId);
    }

    /**
     * 새로운 사용자 등록
     * @param userDTO UserVO 형태로 삽입할 데이터를 받아와야 함
     */
    @PostMapping("register")
    public void registerNewUser(@RequestBody UserDTO userDTO) {
        userService.registerUserInfo(userDTO);
    }

    /**
     * 등록된 사용자 삭제
     * @param cameraId PK인 UNO를 전달하면 삭제함
     */
    @DeleteMapping("remove")
    public void removeUserInfo(String cameraId) {
        userService.removeUserInfo(cameraId);
    }

    /**
     * 사용자 정보 갱신
     * @param userDTO UserVO 형태로 받아와야 하며, 모든 멤버를 다 체워야 한다.
     */
    @PutMapping("modify")
    public void modifyUserInfo(@RequestBody UserDTO userDTO) {
        userService.modifyUserInfo(userDTO);
    }
}
