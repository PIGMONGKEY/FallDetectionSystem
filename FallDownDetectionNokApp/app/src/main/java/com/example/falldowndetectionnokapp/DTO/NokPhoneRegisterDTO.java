package com.example.falldowndetectionnokapp.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NokPhoneRegisterDTO {

    @SerializedName("nokPhone")
    @Expose
    String nokPhone;

    @SerializedName("token")
    @Expose
    String token;

    public NokPhoneRegisterDTO(String nokPhone, String token) {
        this.nokPhone = nokPhone;
        this.token = token;
    }

    public String getNokPhone() {
        return nokPhone;
    }

    public void setNokPhone(String nokPhone) {
        this.nokPhone = nokPhone;
    }
}
