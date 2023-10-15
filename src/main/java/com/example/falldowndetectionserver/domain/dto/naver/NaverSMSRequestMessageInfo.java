package com.example.falldowndetectionserver.domain.dto.naver;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NaverSMSRequestMessageInfo {
    private String to;
}
