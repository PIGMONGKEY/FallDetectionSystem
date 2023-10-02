package com.example.falldowndetectionserver.domain.vo;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Builder
public class UserVO {
    private String cameraId;
    private String userName;
    private String userPassword;
    private String userPhone;
    private String userAddress;
    private String regdate;
    private String updatedate;
    private String userRole;
}
