package com.example.falldowndetectionserver.controller;

import com.example.falldowndetectionserver.domain.dto.BasicResponseDTO;
import com.example.falldowndetectionserver.domain.dto.user.UserInfoRequestDTO;
import com.example.falldowndetectionserver.handler.VideoWebSocketHandler;
import com.example.falldowndetectionserver.jwt.TokenProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.parser.Token;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.nio.charset.StandardCharsets;

/**
 * 라즈베리 파이 카메라 영상 확인을 위한 테스트 page
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class VideoController {
    private final TokenProvider tokenProvider;
    private final VideoWebSocketHandler videoWebSocketHandler;

    @GetMapping("/show")
    public String showVideo() {
        return "show-video";
    }

    @GetMapping("/streaming")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<BasicResponseDTO<String>> getStreamingUrl(@RequestHeader("Authorization") String token, String cameraId) {
        BasicResponseDTO<String> response;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        // 토큰 속에 있는 CameraId와 요청 CameraId가 다르면 서비스 거부
        if (!tokenProvider.getAudience(token.substring(7)).equals(cameraId)) {
            response = BasicResponseDTO.<String>builder()
                    .code(HttpStatus.METHOD_NOT_ALLOWED.value())
                    .httpStatus(HttpStatus.METHOD_NOT_ALLOWED)
                    .message("요청한 작업에 대한 권한이 없습니다.")
                    .build();

            return new ResponseEntity<>(response, httpHeaders, response.getHttpStatus());
        }

        response = BasicResponseDTO.<String>builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("스트리밍 주소 요청 성공")
                .data(videoWebSocketHandler.getStreamingURL(cameraId))
                .build();

        return new ResponseEntity<>(response, httpHeaders, response.getHttpStatus());
    }
}
