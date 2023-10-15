package com.example.falldowndetectionserver.domain.dto.naver;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class NaverSMSRequestDTO {
    private String type;
    private String from;
    private String content;
    private List<NaverSMSRequestMessageInfo> messages;
}
