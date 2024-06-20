package com.example.falldowndetectionnokapp.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BasicResponseDTO<T> {
    @SerializedName("code")
    @Expose
    private Integer code;

    @SerializedName("httpStatus")
    @Expose
    private String httpStatus;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private T data;

    public Integer getCode() {
        return code;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
