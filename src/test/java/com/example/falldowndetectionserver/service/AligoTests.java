package com.example.falldowndetectionserver.service;

import com.example.falldowndetectionserver.service.emergency.EmergencyService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class AligoTests {
    @Autowired
    private EmergencyService emergencyService;

    @Test
    public void sendTest() {
        emergencyService.sendEmergencySMS("cam01");
    }
}
