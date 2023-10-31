package com.example.falldetectionapp.retrofit;

import com.example.falldetectionapp.DTO.BasicResponseDTO;
import com.example.falldetectionapp.DTO.NotificationDTO;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NotificationService {
    @GET("noti")
    Call<BasicResponseDTO<NotificationDTO>> requestAllNotifications();
}
