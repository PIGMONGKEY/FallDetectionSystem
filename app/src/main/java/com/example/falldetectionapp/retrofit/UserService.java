package com.example.falldetectionapp.retrofit;

import com.example.falldetectionapp.DTO.BasicResponseDTO;
import com.example.falldetectionapp.DTO.SignUpDTO;
import com.example.falldetectionapp.DTO.UserInfoDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @GET("user")
    Call<BasicResponseDTO> checkCameraId(@Query("cameraId") String cameraId);

    @GET("user")
    Call<BasicResponseDTO<UserInfoDTO>> getUserInfo(@Header("Authorization") String personalToken,
                                  @Query("cameraId") String cameraId);

    @POST("user")
    Call<BasicResponseDTO> signUp(@Body SignUpDTO signUpDTO);
}
