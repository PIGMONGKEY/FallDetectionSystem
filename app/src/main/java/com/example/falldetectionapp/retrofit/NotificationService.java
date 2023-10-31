package com.example.falldetectionapp.retrofit;

import com.example.falldetectionapp.DTO.BasicResponseDTO;
import com.example.falldetectionapp.DTO.NotificationDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NotificationService {
    @GET("noti")
    Call<BasicResponseDTO<ArrayList<NotificationDTO>>> requestAllNotifications();
}
