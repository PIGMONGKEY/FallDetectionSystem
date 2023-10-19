package com.example.falldowndetectionserver.controller;

import com.example.falldowndetectionserver.dao.NotiBoardDao;
import com.example.falldowndetectionserver.domain.dto.BasicResponseDTO;
import com.example.falldowndetectionserver.domain.vo.NotiBoardVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotiController {
    private final NotiBoardDao notiBoardDao;

    @PostMapping("/noti")
    public ResponseEntity<BasicResponseDTO> newNoti(@RequestBody NotiBoardVO notiBoardVO) {
        BasicResponseDTO response;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        if (notiBoardDao.insert(notiBoardVO) == 1) {
            response = BasicResponseDTO.builder()
                    .code(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .message("공지사항 등록 성공")
                    .build();
        } else {
            response = BasicResponseDTO.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("공지사항 등록 실패")
                    .build();
        }

        return new ResponseEntity<>(response, httpHeaders, response.getHttpStatus());
    }

    @GetMapping("/noti")
    public ResponseEntity<BasicResponseDTO<List<NotiBoardVO>>> getAllNoti() {
        BasicResponseDTO response;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        response = BasicResponseDTO.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("공지사항 불러오기 성공")
                .data(notiBoardDao.selectAll())
                .build();

        return new ResponseEntity<>(response, httpHeaders, response.getHttpStatus());
    }

    @PutMapping("/noti")
    public ResponseEntity<BasicResponseDTO> modifyNoti(@RequestBody NotiBoardVO notiBoardVO) {
        BasicResponseDTO response;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        if (notiBoardDao.update(notiBoardVO) == 1) {
            response = BasicResponseDTO.builder()
                    .code(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .message("공지사항 수정 성공")
                    .build();
        } else {
            response = BasicResponseDTO.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("공지사항 수정 실패")
                    .build();
        }

        return new ResponseEntity<>(response, httpHeaders, response.getHttpStatus());
    }

    @DeleteMapping("/noti")
    public ResponseEntity<BasicResponseDTO> removeNoti(int bno) {
        BasicResponseDTO response;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        if (notiBoardDao.delete(bno) == 1) {
            response = BasicResponseDTO.builder()
                    .code(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .message("공지사항 삭제 성공")
                    .build();
        } else {
            response = BasicResponseDTO.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("공지사항 삭제 실패")
                    .build();
        }

        return new ResponseEntity<>(response, httpHeaders, response.getHttpStatus());
    }
}
