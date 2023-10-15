package com.example.falldowndetectionserver.controller;

import com.example.falldowndetectionserver.domain.dto.BasicResponseDTO;
import com.example.falldowndetectionserver.domain.dto.LoginDTO;
import com.example.falldowndetectionserver.domain.dto.auth.AuthTokenParam;
import com.example.falldowndetectionserver.domain.dto.auth.AuthTokenResponse;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/")
@Slf4j
public class AuthController {
    private final AuthService authService;

    /**
     * 로그인하는 메소드
     * 카메라 아이디와 비밀번호를 넘겨주면 확인 후 토큰을 리턴함
     * @param loginDTO
     * @return
     */
    @PostMapping("login")
    public ResponseEntity<AuthTokenResponse> login(@RequestBody LoginDTO loginDTO) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        AuthTokenResponse authTokenResponse = authService.login(loginDTO);

        if (authTokenResponse.getCode() == HttpStatus.OK.value()) {
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + authTokenResponse.getToken());
            return new ResponseEntity<>(authTokenResponse, httpHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(authTokenResponse, httpHeaders, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 로그아웃하는 메소드
     * @param authTokenParam 토큰을 넘겨주면 redis에 토큰을 저장하고, 이후 이 토큰과 함께 들어오는 토큰은 무시함
     * @return
     */
    @PostMapping("logout")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity logout(@RequestBody AuthTokenParam authTokenParam) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        authService.logout(authTokenParam);

        return new ResponseEntity(BasicResponseDTO.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("로그아웃 성공")
                .build(), httpHeaders, HttpStatus.OK);
    }

}
