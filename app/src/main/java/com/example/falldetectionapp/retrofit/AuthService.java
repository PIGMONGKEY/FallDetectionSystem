package com.example.falldetectionapp.retrofit;

import com.example.falldetectionapp.DTO.AuthTokenDTO;
import com.example.falldetectionapp.DTO.BasicResponseDTO;
import com.example.falldetectionapp.DTO.LoginDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("auth/login")
    Call<BasicResponseDTO<AuthTokenDTO>> requestLogin(@Body LoginDTO loginDTO);
}
