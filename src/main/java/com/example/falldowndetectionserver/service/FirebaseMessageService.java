package com.example.falldowndetectionserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FirebaseMessageService {
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/falldowndetection/messages:send";
    private final ObjectMapper objectMapper;

    private void sendMessageTo(String targetToken, String title, String body) {

    }
}
