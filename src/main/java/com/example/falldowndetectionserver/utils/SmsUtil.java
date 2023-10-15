package com.example.falldowndetectionserver.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.util.Base64;

@Component
@Getter
public class SmsUtil {
    private final String serviceId;
    private final String baseURL = "https://sens.apigw.ntruss.com";
    private final String sendMessagePath;
    private final String accessKey;
    private final String secretKey;

    /**
     * application.yml 에서 Naver key를 읽어온다.
     * @param serviceId
     * @param accessKey
     * @param secretKey
     */
    public SmsUtil(
            @Value("${naver.service-id}") String serviceId,
            @Value("${naver.access-key}") String accessKey,
            @Value("${naver.secret-key}") String secretKey
    ) {
        this.serviceId = serviceId;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        // TODO: URI 생성할 때 문제 안되는 지 확인 필요
        this.sendMessagePath = "/sms/v2/services/" + serviceId + "/messages";
    }

    public URI getSendMessageURI() {
        return UriComponentsBuilder
                    .fromUriString(baseURL)
                    .path(sendMessagePath)
                    .encode()
                    .build()
//                    .expand(serviceId)
                    .toUri();
    }

    public String getSignature(long time) {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String timestamp = time + "";

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(baseURL)
                .append(sendMessagePath)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        try {
            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "");
            Mac mac = Mac.getInstance("");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));

            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            return "fail";
        }
    }
}
