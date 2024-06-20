package com.example.falldowndetectionnokapp.retrofit;

import com.example.falldowndetectionnokapp.DTO.BasicResponseDTO;
import com.example.falldowndetectionnokapp.DTO.NokPhoneRegisterDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface HttpService {

    @POST("nok")
    Call<BasicResponseDTO> requestRegisterToken(@Body NokPhoneRegisterDTO nokPhoneRegisterDTO);
}
