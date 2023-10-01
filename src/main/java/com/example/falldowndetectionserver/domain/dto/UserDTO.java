package com.example.falldowndetectionserver.domain.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class UserDTO {
    private Boolean requestSuccess;
    private String cameraId;
    private String userName;
    private String userPassword;
    private String userPhone;
    private String userAddress;
    private String regdate;
    private String updatedate;
    private String userRole;
    private List<String> nokPhones;
}
