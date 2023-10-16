package com.example.falldetectionapp.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 사용자 정보를 받아올 때 사용하는 DTO 입니다.
 */
public class UserInfoDTO implements Serializable {

    @SerializedName("cameraId")
    @Expose
    private String cameraId;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userPassword")
    @Expose
    private String userPassword;
    @SerializedName("userPhone")
    @Expose
    private String userPhone;
    @SerializedName("userAddress")
    @Expose
    private String userAddress;
    @SerializedName("nokPhones")
    @Expose
    private List<String> nokPhones;

    public String getCameraId() {
        return cameraId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public List<String> getNokPhones() {
        return nokPhones;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public void setNokPhones(List<String> nokPhones) {
        this.nokPhones = nokPhones;
    }
}
