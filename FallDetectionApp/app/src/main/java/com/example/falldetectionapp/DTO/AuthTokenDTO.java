package com.example.falldetectionapp.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthTokenDTO {
    @SerializedName("token")
    @Expose
    private String token;

    public AuthTokenDTO() {
    }

    public AuthTokenDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
