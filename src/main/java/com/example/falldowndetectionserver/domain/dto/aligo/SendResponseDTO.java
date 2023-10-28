package com.example.falldowndetectionserver.domain.dto.aligo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendResponseDTO {
    private final Integer result_code;
    private final String message;
    private final Integer msg_id;
    private final Integer success_cnt;
    private final Integer error_cnt;
    private final String msg_type;
}
