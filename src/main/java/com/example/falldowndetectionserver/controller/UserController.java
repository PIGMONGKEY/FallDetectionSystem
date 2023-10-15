package com.example.falldowndetectionserver.controller;

import com.example.falldowndetectionserver.domain.dto.user.SignUpRequestDTO;
import com.example.falldowndetectionserver.domain.dto.BasicResponseDTO;
import com.example.falldowndetectionserver.domain.dto.user.UserRequestDTO;
import com.example.falldowndetectionserver.domain.dto.user.UserResponseDTO;
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
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        BasicResponseDTO response = userService.checkCameraId(cameraId);

        return new ResponseEntity(response, httpHeaders, response.getHttpStatus());
    }

    /**
     * 회원가입
     * @param signUpRequestDTO UserVO 형태로 삽입할 데이터를 받아와야 함
     * @return 성공 시 OK 보내줌
     */
    @PostMapping("/user")
    public ResponseEntity signup(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        BasicResponseDTO response = userService.signup(signUpRequestDTO);
        return new ResponseEntity(response, httpHeaders, response.getHttpStatus());
    }

    /**
     * 사용자 정보를 받아옴
     * @param cameraId PK인 cameraId를 전달한다.
     * @return 결과 UserVO를 JSON 형태로 리턴한다.
     */
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserResponseDTO> getUserInfo(String cameraId, @RequestHeader("Authorization") String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        UserResponseDTO response;

        // 토큰 속에 있는 CameraId와 요청 CameraId가 다르면 서비스 거부
        if (!tokenProvider.getAudience(token.substring(7)).equals(cameraId)) {
            response = UserResponseDTO.builder()
                    .code(HttpStatus.METHOD_NOT_ALLOWED.value())
                    .httpStatus(HttpStatus.METHOD_NOT_ALLOWED)
                    .message("요청한 작업에 대한 권한이 없습니다.")
                    .build();

            return new ResponseEntity<>(response, httpHeaders, response.getHttpStatus());
        }

        response = userService.getUserInfo(cameraId);

        return new ResponseEntity<>(response, httpHeaders, response.getHttpStatus());
    }

    /**
     * 등록된 사용자 삭제
     * @param cameraId PK인 UNO를 전달하면 삭제함
     */
    @DeleteMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity removeUserInfo(String cameraId, @RequestHeader("Authorization") String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        // 토큰 속에 있는 CameraId와 요청 CameraId가 다르면 서비스 거부
        if (!tokenProvider.getAudience(token.substring(7)).equals(cameraId)) {
            return new ResponseEntity(BasicResponseDTO.builder()
                    .code(HttpStatus.METHOD_NOT_ALLOWED.value())
                    .httpStatus(HttpStatus.METHOD_NOT_ALLOWED)
                    .message("요청한 작업에 대한 권한이 없습니다.")
                    .build(), HttpStatus.METHOD_NOT_ALLOWED);
        }

        BasicResponseDTO response = userService.removeUserInfo(cameraId);

        return new ResponseEntity(response, httpHeaders, response.getHttpStatus());
    }

    /**
     * 사용자 정보 갱신
     * @param userRequestDTO UserDTO 형태로 받아와야 한다.
     */
    @PutMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<BasicResponseDTO> modifyUserInfo(@RequestBody UserRequestDTO userRequestDTO, @RequestHeader("Authorization") String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        // 토큰 속에 있는 CameraId와 요청 CameraId가 다르면 서비스 거부
        if (!tokenProvider.getAudience(token.substring(7)).equals(userRequestDTO.getCameraId())) {
            return new ResponseEntity(BasicResponseDTO.builder()
                    .code(HttpStatus.METHOD_NOT_ALLOWED.value())
                    .httpStatus(HttpStatus.METHOD_NOT_ALLOWED)
                    .message("요청한 작업에 대한 권한이 없습니다.")
                    .build(), httpHeaders, HttpStatus.METHOD_NOT_ALLOWED);
        }

        BasicResponseDTO response = userService.modifyUserInfo(userRequestDTO);

        return new ResponseEntity<>(response, httpHeaders, response.getHttpStatus());
    }
}
