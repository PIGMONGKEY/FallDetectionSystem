package com.example.falldowndetectionserver.domain.dto.user;

import com.example.falldowndetectionserver.domain.vo.UserPhoneTokenVO;
import lombok.Builder;
import lombok.Data;

/**
 * 회원가입 요청 DTO
 */
@Data
@Builder
public class SignUpRequestDTO {
    private final UserPhoneTokenVO phoneToken;
    private final UserInfoRequestDTO userInfo;
}
