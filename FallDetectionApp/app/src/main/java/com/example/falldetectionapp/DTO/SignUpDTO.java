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

    public UserPhoneTokenDTO getPhoneToken() {
        return phoneToken;
    }

    public void setPhoneToken(UserPhoneTokenDTO phoneToken) {
        this.phoneToken = phoneToken;
    }

    public UserInfoDTO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoDTO userInfo) {
        this.userInfo = userInfo;
    }
}
