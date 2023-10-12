package com.example.falldetectionapp.retrofit;

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
    @GET("user/checkCameraId")
    Call<String> checkCameraId(@Query("cameraId") String cameraId);

    @GET("user/info")
    Call<UserInfoDTO> getUserInfo(@Header("Authorization") String personalToken,
                                  @Query("cameraId") String cameraId);

    @POST("user/signup")
    Call<String> signUp(@Body SignUpDTO signUpDTO);
}
