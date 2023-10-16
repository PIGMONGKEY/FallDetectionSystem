package com.example.falldowndetectionserver.domain.dto.user;

import com.example.falldowndetectionserver.domain.vo.UserPhoneTokenVO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpRequestDTO {
    private final UserPhoneTokenVO phoneToken;
    private final UserRequestDTO userInfo;
}
