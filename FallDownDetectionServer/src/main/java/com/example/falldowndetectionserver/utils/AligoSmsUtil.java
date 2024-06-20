package com.example.falldowndetectionserver.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@Getter
public class AligoSmsUtil {
    private final String key;
    private final String host;
    private final String user_id;
    private final String sender;

    public AligoSmsUtil(@Value("${aligo.key}") String key,
                        @Value("${aligo.host}") String host,
                        @Value("${aligo.user_id}") String user_id,
                        @Value("${aligo.sender}") String sender) {
        this.key = key;
        this.host = host;
        this.user_id = user_id;
        this.sender = sender;
    }

    public String getSendRequestURI() {
        return "https://" + host + "/send/";
    }
}
