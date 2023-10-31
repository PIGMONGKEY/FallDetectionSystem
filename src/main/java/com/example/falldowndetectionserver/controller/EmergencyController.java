package com.example.falldowndetectionserver.controller;

import com.example.falldowndetectionserver.dao.UPTokenDao;
import com.example.falldowndetectionserver.domain.dto.BasicResponseDTO;
import com.example.falldowndetectionserver.fallDownDetect.FallDownDetector;
import com.example.falldowndetectionserver.service.emergency.EmergencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * 위급상황 처리 API
 * 위급 SMS 전송, 위급상황 해제
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/emergency/")
public class EmergencyController {
    private final EmergencyService emergencyService;
    private final UPTokenDao uPTokenDao;

    /**
     * 위급상황을 해제한다.
     * 사용자 기기 토큰으로 cameraId를 조회한 후 위급상황을 해제한다.
     * @param uptoken 사용자 기기 토큰을 파라미터로 받는다
     */
    @GetMapping("release")
    public ResponseEntity<BasicResponseDTO> releaseEmergencySituation(String uptoken) {
        // 기기 토큰으로 cameraId를 받는다.
        String cameraId = uPTokenDao.selectCameraId(uptoken).get();
        emergencyService.emergencyRelease(cameraId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        BasicResponseDTO responseBody = BasicResponseDTO.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("위급상황 해제 완료")
                .build();

        return new ResponseEntity<>(responseBody, httpHeaders, responseBody.getHttpStatus());
    }

    /**
     * 위급 SMS를 전송한다.
     * 사용자 기기 토큰으로 cameraId를 조횐한 후
     * 해당 cameraId로 저장된 보호자 연락처에게 위급 메시지를 보낸다.
     * @param uptoken 사용자 기기 토큰을 파라미터로 받는다
     */
    @GetMapping("sos")
    public ResponseEntity<BasicResponseDTO> sendEmergencySms(String uptoken) {
        // 기기 토큰으로 cameraId를 받는다.
        String cameraId = uPTokenDao.selectCameraId(uptoken).get();
        emergencyService.sendEmergencySMS(cameraId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        BasicResponseDTO responseBody = BasicResponseDTO.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("긴급 도음 요청 성공")
                .build();

        return new ResponseEntity<>(responseBody, httpHeaders, responseBody.getHttpStatus());
    }
}
