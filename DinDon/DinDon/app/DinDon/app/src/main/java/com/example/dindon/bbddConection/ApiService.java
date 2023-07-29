package com.example.dindon.bbddconection;

import com.example.dindon.DTOFront.Pisos;

import java.util.List;
import retrofit2. Call;
import retrofit2.http.GET;
public interface ApiService {
    @GET("/pisos")
    Call<List<Pisos>> obtenerPisos();
}