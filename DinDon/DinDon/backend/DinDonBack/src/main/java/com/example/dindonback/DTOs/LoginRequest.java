package com.example.dindonback.DTOs;

public class LoginRequest {
    private String email;
    private String password;

    // Getters y Setters

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
