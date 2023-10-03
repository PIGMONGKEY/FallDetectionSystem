package com.example.falldowndetectionserver.controller;

import com.example.falldowndetectionserver.domain.dto.UserDTO;
import com.example.falldowndetectionserver.jwt.TokenProvider;
import com.example.falldowndetectionserver.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/")
@Slf4j
public class UserController {
    private final UserService userService;
    private final TokenProvider tokenProvider;

    /**
     * 회원가입
     * @param userDTO UserVO 형태로 삽입할 데이터를 받아와야 함
     * @return 성공 시 OK 보내줌
     */
    @PostMapping("signup")
    public ResponseEntity<String> signup(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.signup(userDTO));
    }

    /**
     * 사용자 정보를 받아옴
     * @param cameraId PK인 UNO를 전달해준다
     * @return 결과 UserVO를 JSON 형태로 리턴한다.
     */
    @GetMapping("info")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserDTO> getUserInfo(String cameraId, @RequestHeader("Authorization") String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        UserDTO userDTO;

        if (!tokenProvider.getAudience(token.substring(7)).equals(cameraId)) {
            userDTO = UserDTO.builder()
                    .requestSuccess("NOT ALLOWED")
                    .build();
            return new ResponseEntity<>(userDTO, httpHeaders, HttpStatus.OK);
        }

        userDTO = userService.getUserInfo(cameraId);

        return new ResponseEntity<>(userDTO, httpHeaders, HttpStatus.OK);
    }

    /**
     * 등록된 사용자 삭제
     * @param cameraId PK인 UNO를 전달하면 삭제함
     */
    @DeleteMapping("remove")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> removeUserInfo(String cameraId, @RequestHeader("Authorization") String token) {
        if (!tokenProvider.getAudience(token.substring(7)).equals(cameraId)) {
            return ResponseEntity.ok("NOT ALLOWED");
        }
        return ResponseEntity.ok(userService.removeUserInfo(cameraId));
    }

    /**
     * 사용자 정보 갱신
     * @param userDTO UserVO 형태로 받아와야 하며, 모든 멤버를 다 체워야 한다.
     */
    @PutMapping("modify")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> modifyUserInfo(@RequestBody UserDTO userDTO, @RequestHeader("Authorization") String token) {
        if (!tokenProvider.getAudience(token.substring(7)).equals(userDTO.getCameraId())) {
            return ResponseEntity.ok("NOT ALLOWED");
        }
        return ResponseEntity.ok(userService.modifyUserInfo(userDTO));
    }
}
