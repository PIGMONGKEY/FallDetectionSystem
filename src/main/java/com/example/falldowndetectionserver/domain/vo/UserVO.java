package com.example.falldowndetectionserver.domain.vo;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Builder
public class UserVO {
    private final String cameraId;
    private final String userPassword;
    private final String userName;
    private final String userAge;
    private final String userGender;
    private final String userBloodType;
    private final String userPhone;
    private final String userAddress;
    private final String userRole;
    private final String regdate;
    private final String updatedate;
}
