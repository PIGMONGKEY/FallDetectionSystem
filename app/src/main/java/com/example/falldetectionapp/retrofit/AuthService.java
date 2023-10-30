package com.example.falldetectionapp.retrofit;

import com.example.falldetectionapp.DTO.AuthTokenDTO;
import com.example.falldetectionapp.DTO.BasicResponseDTO;
import com.example.falldetectionapp.DTO.LoginDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthService {
    @POST("auth/login")
    Call<BasicResponseDTO<AuthTokenDTO>> requestLogin(@Body LoginDTO loginDTO);

    @POST("auth/logout")
    Call<BasicResponseDTO<AuthTokenDTO>> requestLogout(@Header("Authorization") String personalToken, @Body AuthTokenDTO authTokenDTO);
}
