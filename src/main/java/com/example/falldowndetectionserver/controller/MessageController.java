package com.example.falldowndetectionserver.controller;

import com.example.falldowndetectionserver.dao.UPTokenDao;
import com.example.falldowndetectionserver.domain.dto.FCMTestRequestDTO;
import com.example.falldowndetectionserver.domain.vo.UserPhoneTokenVO;
import com.example.falldowndetectionserver.service.FirebaseMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/fcm/")
public class MessageController {
    private final FirebaseMessageService firebaseMessageService;
    private final UPTokenDao uPTokenDao;

    @PostMapping("test")
    public ResponseEntity pushTest(@RequestBody FCMTestRequestDTO requestDTO) throws IOException {
        firebaseMessageService.sendMessageTo(
                requestDTO.getToken(),
                requestDTO.getTitle(),
                requestDTO.getBody()
        );

        return ResponseEntity.ok().build();
    }

    @PutMapping("update-token")
    public ResponseEntity updateUserPhoneToken(@RequestBody UserPhoneTokenVO userPhoneTokenVO) {
        if (uPTokenDao.update(userPhoneTokenVO) == 1) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.ok("Fail");
        }
    }
}
