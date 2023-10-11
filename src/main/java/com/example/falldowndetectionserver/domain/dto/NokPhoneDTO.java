package com.example.falldowndetectionserver.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NokPhoneDTO {
    private final String cameraId;
    private final List<String> nokPhones;
}
