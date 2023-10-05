package com.example.falldowndetectionserver.controller;

import com.example.falldowndetectionserver.domain.dto.FcmMessageDTO;
import com.example.falldowndetectionserver.domain.dto.RequestDTO;
import com.example.falldowndetectionserver.service.FirebaseMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/fcm/")
public class MessageController {
    private final FirebaseMessageService firebaseMessageService;

    @PostMapping("test")
    public ResponseEntity pushMessage(@RequestBody RequestDTO requestDTO) throws IOException {
        firebaseMessageService.sendMessageTo(
                requestDTO.getToken(),
                requestDTO.getTitle(),
                requestDTO.getBody()
        );

        return ResponseEntity.ok().build();
    }
}
