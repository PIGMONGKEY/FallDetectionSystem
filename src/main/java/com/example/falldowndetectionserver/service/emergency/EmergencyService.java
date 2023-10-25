package com.example.falldowndetectionserver.service.emergency;

public interface EmergencyService {
    public void sendSMS(String cameraId);

    public void emergencyRelease(String cameraId);
}
