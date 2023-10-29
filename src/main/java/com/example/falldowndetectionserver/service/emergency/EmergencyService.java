package com.example.falldowndetectionserver.service.emergency;

import com.example.falldowndetectionserver.domain.dto.aligo.AligoSendSMSResponseDTO;
import org.springframework.http.ResponseEntity;

public interface EmergencyService {
    public void sendEmergencySMS(String cameraId);
    public void emergencyRelease(String cameraId);
}
