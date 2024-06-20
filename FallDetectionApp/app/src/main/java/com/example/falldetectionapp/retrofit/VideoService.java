package com.example.falldetectionapp.retrofit;

import com.example.falldetectionapp.DTO.BasicResponseDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface VideoService {
    @GET("streaming")
    Call<BasicResponseDTO<String>> requestStreamingUrl(@Header("Authorization") String personalToken, @Query("cameraId") String cameraId);
}
