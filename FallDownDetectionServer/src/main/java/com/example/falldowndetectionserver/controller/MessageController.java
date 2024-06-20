package com.example.falldowndetectionserver.controller;

import com.example.falldowndetectionserver.dao.UPTokenDao;
import com.example.falldowndetectionserver.domain.dto.FCMTestRequestDTO;
import com.example.falldowndetectionserver.service.FirebaseMessageService;
import com.example.falldowndetectionserver.service.emergency.EmergencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * fcm 테스트 controller
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/fcm/")
public class MessageController {
    private final FirebaseMessageService firebaseMessageService;
    private final EmergencyService emergencyService;
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
}
