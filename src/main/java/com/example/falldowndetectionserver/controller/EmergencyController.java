package com.example.falldowndetectionserver.controller;

import com.example.falldowndetectionserver.dao.UPTokenDao;
import com.example.falldowndetectionserver.fallDownDetect.FallDownDetector;
import com.example.falldowndetectionserver.service.emergency.EmergencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/emergency/")
public class EmergencyController {
    private final EmergencyService emergencyService;
    private final UPTokenDao uPTokenDao;

    @GetMapping("release")
    public void releaseEmergencySituation(String uptoken) {
        String cameraId = uPTokenDao.selectCameraId(uptoken).get();
        emergencyService.emergencyRelease(cameraId);
    }

    @GetMapping("sos")
    public void sendEmergencySms(String uptoken) {
        String cameraId = uPTokenDao.selectCameraId(uptoken).get();
        emergencyService.sendEmergencySMS(cameraId);
    }
}
