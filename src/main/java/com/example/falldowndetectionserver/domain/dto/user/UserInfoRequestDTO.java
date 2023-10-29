package com.example.falldowndetectionserver.domain.dto.user;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 사용자 정보 DTO
 */
@Data
@Builder
public class UserInfoRequestDTO {
    private final String cameraId;
    private final String userPassword;
    private final String userName;
    private final int userAge;
    private final String userGender;
    private final String userBloodType;
    private final String userPhone;
    private final String userAddress;
    private final List<String> nokPhones;
}
