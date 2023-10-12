package com.example.falldetectionapp.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpDTO {
    @SerializedName("phoneToken")
    @Expose
    private UserPhoneTokenDTO phoneToken;

    @SerializedName("userInfo")
    @Expose
    private UserInfoDTO userInfo;
}
