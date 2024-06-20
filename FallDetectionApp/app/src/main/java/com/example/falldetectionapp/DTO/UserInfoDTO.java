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
    @SerializedName("userPassword")
    @Expose
    private String userPassword;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userAge")
    @Expose
    private int userAge;
    @SerializedName("userGender")
    @Expose
    private String userGender;
    @SerializedName("userBloodType")
    @Expose
    private String userBloodType;
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

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserAge() {
        return userAge;
    }

    public void setUserAge(Integer userAge) {
        this.userAge = userAge;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserBloodType() {
        return userBloodType;
    }

    public void setUserBloodType(String userBloodType) {
        this.userBloodType = userBloodType;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public List<String> getNokPhones() {
        return nokPhones;
    }

    public void setNokPhones(List<String> nokPhones) {
        this.nokPhones = nokPhones;
    }
}
