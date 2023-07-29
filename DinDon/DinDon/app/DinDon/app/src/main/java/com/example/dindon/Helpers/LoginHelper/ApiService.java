package com.example.dindon.Helpers.LoginHelper;

import com.example.dindon.DTOFront.LoginRequest;
import com.example.dindon.DTOFront.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers("Accept: application/json")
    @POST("api/auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);
}
