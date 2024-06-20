package com.example.falldetectionapp.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginDTO {
    public LoginDTO(String cameraId, String password, String fcmDeviceToken) {
        this.cameraId = cameraId;
        this.password = password;
        this.fcmDeviceToken = fcmDeviceToken;
    }

    @SerializedName("cameraId")
    @Expose
    private String cameraId;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("deviceToken")
    @Expose
    private String fcmDeviceToken;

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFcmDeviceToken(String fcmDeviceToken) {
        this.fcmDeviceToken = fcmDeviceToken;
    }
}
