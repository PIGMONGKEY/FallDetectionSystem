package com.example.falldowndetectionserver.domain.dto.user;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserRequestDTO {
    private final String cameraId;
    private final String userName;
    private final String userPassword;
    private final String userPhone;
    private final String userAddress;
    private final List<String> nokPhones;
}
