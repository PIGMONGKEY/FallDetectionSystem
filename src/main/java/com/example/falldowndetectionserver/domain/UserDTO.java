package com.example.falldowndetectionserver.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class UserDTO {
    private int uno;
    private String cameraId;
    private String userName;
    private String userPassword;
    private String userPhone;
    private String userAddress;
    private String regdate;
    private String updatedate;
    private List<String> nokPhones;
}
