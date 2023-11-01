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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 위급상황 처리 API
 * 위급 SMS 전송, 위급상황 해제
 */
@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/emergency/")
public class EmergencyController {
    private final EmergencyService emergencyService;
    private final UPTokenDao uPTokenDao;
    private final FallDownDetector detector;

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

    @GetMapping("check")
    public String toCheckPage() {
        return "emergency-check";
    }

    @GetMapping("video")
    public String toVideoPage(String cameraId, Model model) {
        model.addAttribute("video_path",  "/emergency/getvid?cameraId=" + cameraId);

        return "emergency-video";
    }

    @GetMapping("getvid")
    public ResponseEntity<StreamingResponseBody> responseFallDownVideo(String cameraId) {
        DateFormat formatter = new SimpleDateFormat("YYYY-MM-DD_HH:mm:ss");
        Date date = new Date(detector.getFallDownTimeHash().get(cameraId));
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        String fileName = formatter.format(date);

        File file = new File("~/falldown/video/" + fileName + ".mp4");

        StreamingResponseBody streamingResponseBody = new StreamingResponseBody() {
            @Override
            public void writeTo(OutputStream outputStream) throws IOException {
                try {
                    final InputStream inputStream = new FileInputStream(file);

                    byte[] bytes = new byte[2048];
                    int length;

                    while ((length = inputStream.read(bytes)) >= 0) {
                        outputStream.write(bytes, 0, length);
                    }

                    inputStream.close();
                    outputStream.flush();
                } catch (Exception e) {
                    log.error("Exception while reading streaming data");
                }
            }
        };

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "video/mp4");
        httpHeaders.add("Content-Length", Long.toString(file.length()));

        return ResponseEntity.ok().headers(httpHeaders).body(streamingResponseBody);
    }
}
