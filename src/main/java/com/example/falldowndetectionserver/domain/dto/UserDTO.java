package com.example.falldowndetectionserver.domain.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Builder
public class UserDTO {
    private final Boolean requestSuccess;
    private final String cameraId;
    private final String userName;
    private final String userPassword;
    private final String userPhone;
    private final String userAddress;
    private final String regdate;
    private final String updatedate;
    private final String userRole;
    private final List<String> nokPhones;
}
