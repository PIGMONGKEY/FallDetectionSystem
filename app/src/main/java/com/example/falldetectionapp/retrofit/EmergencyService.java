package com.example.falldetectionapp.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EmergencyService {

    @GET("emergency/release")
    Call<String> releaseEmergency(@Query("uptoken") String uptoken);

    @GET("emergency/sos")
    Call<String> sos(@Query("uptoken") String uptoken);
}
