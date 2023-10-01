package com.example.falldowndetectionserver.controller;

import com.example.falldowndetectionserver.domain.dto.LoginDTO;
import com.example.falldowndetectionserver.domain.dto.TokenDTO;
import com.example.falldowndetectionserver.domain.dto.UserDTO;
import com.example.falldowndetectionserver.jwt.JwtFilter;
import com.example.falldowndetectionserver.jwt.TokenProvider;
import com.example.falldowndetectionserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user/")
public class UserController {
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    /**
     * 사용자 정보를 받아옴
     * @param cameraId PK인 UNO를 전달해준다
     * @return 결과 UserVO를 JSON 형태로 리턴한다.
     */
    @GetMapping("info")
    public ResponseEntity<UserDTO> getUserInfo(String cameraId) {
        UserDTO userDTO = userService.getUserInfo(cameraId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(userDTO, httpHeaders, HttpStatus.OK);
    }

    /**
     * 회원가입
     * @param userDTO UserVO 형태로 삽입할 데이터를 받아와야 함
     * @return 성공 시 OK 보내줌
     */
    @PostMapping("signup")
    public ResponseEntity<String> signup(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.signup(userDTO));
    }

    @PostMapping("login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getCameraId(), loginDTO.getPassword());

        Authentication authentication = authenticationManagerBuilder
                .getObject()
                .authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDTO(jwt), httpHeaders, HttpStatus.OK);
    }

    /**
     * 등록된 사용자 삭제
     * @param cameraId PK인 UNO를 전달하면 삭제함
     */
    @DeleteMapping("remove")
    public String removeUserInfo(String cameraId) {
        if (userService.removeUserInfo(cameraId) == 1) {
            return "success";
        } else {
            return "fail";
        }
    }

    /**
     * 사용자 정보 갱신
     * @param userDTO UserVO 형태로 받아와야 하며, 모든 멤버를 다 체워야 한다.
     */
    @PutMapping("modify")
    public String modifyUserInfo(@RequestBody UserDTO userDTO) {
        int code = userService.modifyUserInfo(userDTO);
        if (code == 1) {
            return "success";
        } else if (code == -1) {
            return "user fail";
        } else {
            return "nokphone fail";
        }
    }
}
