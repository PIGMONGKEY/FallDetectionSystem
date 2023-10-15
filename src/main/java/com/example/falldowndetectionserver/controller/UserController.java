package com.example.falldowndetectionserver.controller;

import com.example.falldowndetectionserver.domain.dto.user.SignUpRequestDTO;
import com.example.falldowndetectionserver.domain.dto.UserDTO;
import com.example.falldowndetectionserver.domain.dto.BasicResponseDTO;
import com.example.falldowndetectionserver.jwt.TokenProvider;
import com.example.falldowndetectionserver.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final TokenProvider tokenProvider;

    @GetMapping("/checkCameraId")
    public ResponseEntity checkCameraId(String cameraId) {
        BasicResponseDTO response = userService.checkCameraId(cameraId);

        return new ResponseEntity(response, response.getHttpStatus());
    }

    /**
     * 회원가입
     * @param signUpRequestDTO UserVO 형태로 삽입할 데이터를 받아와야 함
     * @return 성공 시 OK 보내줌
     */
    @PostMapping("/user")
    public ResponseEntity signup(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        BasicResponseDTO response = userService.signup(signUpRequestDTO);
        return new ResponseEntity(response, response.getHttpStatus());
    }

    /**
     * 사용자 정보를 받아옴
     * @param cameraId PK인 cameraId를 전달한다.
     * @return 결과 UserVO를 JSON 형태로 리턴한다.
     */
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserDTO> getUserInfo(String cameraId, @RequestHeader("Authorization") String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        UserDTO userDTO;

        // 토큰 속에 있는 CameraId와 요청 CameraId가 다르면 서비스 거부
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
    @DeleteMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> removeUserInfo(String cameraId, @RequestHeader("Authorization") String token) {
        // 토큰 속에 있는 CameraId와 요청 CameraId가 다르면 서비스 거부
        if (!tokenProvider.getAudience(token.substring(7)).equals(cameraId)) {
            return ResponseEntity.ok("NOT ALLOWED");
        }

        return ResponseEntity.ok(userService.removeUserInfo(cameraId));
    }

    /**
     * 사용자 정보 갱신
     * @param userDTO UserDTO 형태로 받아와야 한다.
     */
    @PutMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> modifyUserInfo(@RequestBody UserDTO userDTO, @RequestHeader("Authorization") String token) {
        // 토큰 속에 있는 CameraId와 요청 CameraId가 다르면 서비스 거부
        if (!tokenProvider.getAudience(token.substring(7)).equals(userDTO.getCameraId())) {
            return ResponseEntity.ok("NOT ALLOWED");
        }

        return ResponseEntity.ok(userService.modifyUserInfo(userDTO));
    }
}
