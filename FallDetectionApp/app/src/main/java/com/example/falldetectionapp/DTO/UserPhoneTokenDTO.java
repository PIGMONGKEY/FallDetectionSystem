package com.example.falldetectionapp.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserPhoneTokenDTO {
    @SerializedName("cameraId")
    @Expose
    private String cameraId;

    @SerializedName("token")
    @Expose
    private String token;

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
