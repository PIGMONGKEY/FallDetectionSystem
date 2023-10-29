package com.example.falldowndetectionserver.controller;

import com.example.falldowndetectionserver.domain.dto.BasicResponseDTO;
import com.example.falldowndetectionserver.domain.dto.user.LoginRequestDTO;
import com.example.falldowndetectionserver.domain.dto.auth.AuthTokenParam;
import com.example.falldowndetectionserver.jwt.JwtFilter;
import com.example.falldowndetectionserver.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * 인증 API
 * 로그인과 로그아웃을 담당한다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/")
@Slf4j
public class AuthController {
    private final AuthService authService;

    /**
     * 로그인하는 메소드
     * 카메라 아이디와 비밀번호를 넘겨주면 확인 후 토큰을 리턴함
     * @param loginRequestDTO
     * @return
     */
    @PostMapping("login")
    public ResponseEntity<BasicResponseDTO<AuthTokenParam>> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        BasicResponseDTO<AuthTokenParam> response = authService.login(loginRequestDTO);

        if (response.getCode() == HttpStatus.OK.value()) {
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + response.getData().getToken());
            return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, httpHeaders, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 로그아웃하는 메소드
     * @param authTokenParam 토큰을 넘겨주면 redis에 토큰을 저장하고, 이후 이 토큰과 함께 들어오는 토큰은 무시함
     * @return
     */
    @PostMapping("logout")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<BasicResponseDTO> logout(@RequestBody AuthTokenParam authTokenParam) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        authService.logout(authTokenParam);

        return new ResponseEntity<>(BasicResponseDTO.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("로그아웃 성공")
                .build(), httpHeaders, HttpStatus.OK);
    }

}
