package com.example.falldetectionapp.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationDTO {
    @SerializedName("bno")
    @Expose
    private Integer bno;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("content")
    @Expose
    private String notiContent;

    @SerializedName("regdate")
    @Expose
    private String regdate;

    @SerializedName("updatedate")
    @Expose
    private String updatedate;

    public Integer getBno() {
        return bno;
    }

    public String getTitle() {
        return title;
    }

    public String getNotiContent() {
        return notiContent;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getUpdatedate() {
        return updatedate;
    }
}
