package com.example.falldowndetectionserver.domain.dto;

import com.example.falldowndetectionserver.domain.vo.UserPhoneTokenVO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpDTO {
    private final UserPhoneTokenVO phoneToken;
    private final UserDTO userInfo;
}
