package com.example.dindon.DTOFront;


import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("token")
    private String token;

    @SerializedName("message")
    private String message;

    @SerializedName("email")
    private String email;

    // Añade un constructor vacío
    public LoginResponse() {
    }
    public String getEmail() {
        return email;
    }
    // Otros constructores, getters y setters
}
