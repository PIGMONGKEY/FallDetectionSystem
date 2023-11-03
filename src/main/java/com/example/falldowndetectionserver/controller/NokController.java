package com.example.falldowndetectionserver.controller;

import com.example.falldowndetectionserver.dao.NokPhoneDao;
import com.example.falldowndetectionserver.domain.dto.BasicResponseDTO;
import com.example.falldowndetectionserver.domain.vo.NokPhoneVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@RestController
public class NokController {
    private final NokPhoneDao nokPhoneDao;

    @PostMapping("nok")
    public ResponseEntity<BasicResponseDTO> registerNokphoneToken(@RequestBody NokPhoneVO nokPhoneVO) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        BasicResponseDTO responseBody;

        if (nokPhoneDao.updateToken(nokPhoneVO) == 1) {
            responseBody = BasicResponseDTO.builder()
                    .code(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .message("보호자 핸드폰 토큰 저장 완료")
                    .build();
        } else {
            responseBody = BasicResponseDTO.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("보호자 핸드폰 기기토큰 저장 실패")
                    .build();
        }

        return new ResponseEntity<>(responseBody, httpHeaders, responseBody.getHttpStatus());
    }
}
