package com.example.falldowndetectionserver.domain.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotiBoardVO {
    private final Integer bno;
    private final String title;
    private final String content;
    private final String regdate;
    private final String updatedate;
}
