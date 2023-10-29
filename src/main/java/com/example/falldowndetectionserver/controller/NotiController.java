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

/**
 * 공지사항 API Controller
 */
@RestController
@RequiredArgsConstructor
public class NotiController {
    private final NotiBoardDao notiBoardDao;

    /**
     * 새로운 공지사항을 등록한다.
     * @param notiBoardVO NotiBoardVO형식으로 JSON 형태로 받는다.
     * @return BasicResponseDTO를 JSON 형태로 응답한다.
     */
    @PostMapping("/noti")
    public ResponseEntity<BasicResponseDTO> newNoti(@RequestBody NotiBoardVO notiBoardVO) {
        BasicResponseDTO response;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        // 성공하면 200 리턴
        if (notiBoardDao.insert(notiBoardVO) == 1) {
            response = BasicResponseDTO.builder()
                    .code(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .message("공지사항 등록 성공")
                    .build();
        } else {
        // 실패하면 500 리턴
            response = BasicResponseDTO.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("공지사항 등록 실패")
                    .build();
        }

        return new ResponseEntity<>(response, httpHeaders, response.getHttpStatus());
    }

    /**
     * 모든 공지사항을 조회한다.
     * @return BasicResponseDTO에 NotiBoardVO를 리스트로 담아서 JSON으로 반환한다.
     */
    @GetMapping("/noti")
    public ResponseEntity<BasicResponseDTO<List<NotiBoardVO>>> getAllNoti() {
        BasicResponseDTO response;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        List<NotiBoardVO> notices = notiBoardDao.selectAll();

        // 공지사항이 없다면 404 리턴
        if (notices.isEmpty()) {
            response = BasicResponseDTO.builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message("등록된 공지사항이 없습니다.")
                    .build();
        } else {
        // 있다면 공지사항 리턴
            response = BasicResponseDTO.builder()
                    .code(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .message("공지사항 불러오기 성공")
                    .data(notices)
                    .build();
        }

        return new ResponseEntity<>(response, httpHeaders, response.getHttpStatus());
    }

    /**
     * 공지사항 수정
     * @param notiBoardVO notiBoardVO 형태로 받아서 수정한다.
     */
    @PutMapping("/noti")
    public ResponseEntity<BasicResponseDTO> modifyNoti(@RequestBody NotiBoardVO notiBoardVO) {
        BasicResponseDTO response;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        // 수정 성공하면 200 리턴
        if (notiBoardDao.update(notiBoardVO) == 1) {
            response = BasicResponseDTO.builder()
                    .code(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .message("공지사항 수정 성공")
                    .build();
        } else {
        // 실패하면 500 리턴
            response = BasicResponseDTO.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("공지사항 수정 실패")
                    .build();
        }

        return new ResponseEntity<>(response, httpHeaders, response.getHttpStatus());
    }

    /**
     * 공지사항 삭제
     * @param bno 공지사항 번호를 파라미터로 받음
     */
    @DeleteMapping("/noti")
    public ResponseEntity<BasicResponseDTO> removeNoti(int bno) {
        BasicResponseDTO response;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        // 삭제 성공하면 200 리턴
        if (notiBoardDao.delete(bno) == 1) {
            response = BasicResponseDTO.builder()
                    .code(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .message("공지사항 삭제 성공")
                    .build();
        } else {
        // 삭제 실패하면 500 리턴
            response = BasicResponseDTO.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("공지사항 삭제 실패")
                    .build();
        }

        return new ResponseEntity<>(response, httpHeaders, response.getHttpStatus());
    }
}
