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
}
