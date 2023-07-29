package com.example.dindon.dtofront;


import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("token")
    private String token;

    @SerializedName("message")
    private String message;

    @SerializedName("email")
    private String email;

    public LoginResponse() {
        //No se usa
    }
    public String getEmail() {
        return email;
    }
}
