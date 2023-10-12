package com.example.falldetectionapp.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginDTO {
    public LoginDTO(String cameraId, String password) {
        this.cameraId = cameraId;
        this.password = password;
    }

    @SerializedName("cameraId")
    @Expose
    private String cameraId;

    @SerializedName("password")
    @Expose
    private String password;

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
