package com.example.falldowndetectionserver.domain.dto.aligo;

import lombok.Builder;
import lombok.Data;

/**
 * Aligo 문자전송 시스템에 SMS 전송 요청 후 응답 DTO
 */
@Data
@Builder
public class AligoSendSMSResponseDTO {
    private final Integer result_code;
    private final String message;
    private final Integer msg_id;
    private final Integer success_cnt;
    private final Integer error_cnt;
    private final String msg_type;
}
