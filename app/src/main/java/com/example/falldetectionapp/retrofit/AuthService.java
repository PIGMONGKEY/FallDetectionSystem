package com.example.falldetectionapp.retrofit;

import com.example.falldetectionapp.DTO.LoginDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    public static String token = null;

    @POST("/auth/login")
    Call<String> requestLogin(@Body LoginDTO loginDTO);
}
