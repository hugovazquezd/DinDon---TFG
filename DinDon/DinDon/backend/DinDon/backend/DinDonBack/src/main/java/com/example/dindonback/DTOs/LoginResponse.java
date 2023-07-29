package com.example.dindonback.DTOs;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonRootName("response")
public class LoginResponse {
    @JsonProperty("token")
    private String token;

    @JsonProperty("message")
    private String message;

    @JsonProperty("email")
    private String email;

    public LoginResponse(String token, String message, String email) {
        this.token = token;
        this.message = message;
        this.email = email;
    }
}
