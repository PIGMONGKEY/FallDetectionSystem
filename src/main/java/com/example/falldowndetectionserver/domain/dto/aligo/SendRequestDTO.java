package com.example.falldowndetectionserver.domain.dto.aligo;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class SendRequestDTO {
    private final String key;
    private final String user_id;
    private final String sender;
    private final String receiver;
    private final String msg;
    private final String msg_type;
    private final String testmode_yn;   // Y or null
}
