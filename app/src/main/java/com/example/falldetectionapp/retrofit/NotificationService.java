package com.example.falldetectionapp.retrofit;

import com.example.falldetectionapp.DTO.BasicResponseDTO;
import com.example.falldetectionapp.DTO.NotificationDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NotificationService {
    @GET("noti")
    Call<BasicResponseDTO<ArrayList<NotificationDTO>>> requestAllNotifications();

    @GET("noti/{bno}")
    Call<BasicResponseDTO<NotificationDTO>> requestOneNotification(@Path("bno") int bno);
}
