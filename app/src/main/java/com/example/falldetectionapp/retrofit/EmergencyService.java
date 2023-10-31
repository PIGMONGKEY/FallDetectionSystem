package com.example.falldetectionapp.retrofit;

import com.example.falldetectionapp.DTO.BasicResponseDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EmergencyService {

    @GET("emergency/release")
    Call<BasicResponseDTO> releaseEmergency(@Query("uptoken") String uptoken);

    @GET("emergency/sos")
    Call<BasicResponseDTO> sos(@Query("uptoken") String uptoken);
}
