package com.example.falldowndetectionserver.service;

import com.example.falldowndetectionserver.domain.dto.FcmMessageDTO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FirebaseMessageService {
    private final String firebaseConfigPath = "firebase/falldetectionsystem-d2da0-firebase-adminsdk-cm9z5-78dd1e3a95.json";
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/falldetectionsystem-d2da0/messages:send";
    private final ObjectMapper objectMapper;

    /**
     * 전송할 목적지 토큰, 제목, 내용을 받아서, push알림을 발송하는 메소드
     * @param targetToken 목적지 token을 받는다. - Android에서 발행할 수 있다.
     * @param title 제목
     * @param body 내용
     * @throws IOException
     */
    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        // JSON 형식의 메시지 생성
        String message = makeMessage(targetToken, title, body);

        OkHttpClient client = new OkHttpClient();
        // 요청 바다 생성
        RequestBody requestBody = RequestBody.create(message,
                MediaType.get("application/json; charset=utf-8"));
        // Request 객체 생성
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        // Push 알림 발송 요청
        Response response = client.newCall(request).execute();

        System.out.println(response.body().toString());
    }

    /**
     * 목적지 token, 제목, 내용을 받아서 FcmMessageDTO 형태로 만든 후, JSON 형태의 문자열로 리턴한다.
     * @param targetToken 목적지 token
     * @param title 제목
     * @param body 내용
     * @return JSON 형태의 FcmMessageDTO
     * @throws JsonParseException
     * @throws JsonProcessingException
     */
    private String makeMessage(String targetToken, String title, String body) throws JsonParseException, JsonProcessingException {
        FcmMessageDTO fcmMessage = FcmMessageDTO.builder()
                .message(FcmMessageDTO.Message.builder()
                        .token(targetToken)
                        .data(FcmMessageDTO.Data.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    /**
     * fcm에서 발급받은 admin sdk json 파일에서 정보를 가져와서 AccessToken을 발급받는다.
     * @return String 형태의 토큰을 리턴한다.
     * @throws IOException
     */
    private String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
