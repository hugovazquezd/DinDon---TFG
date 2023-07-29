package com.example.dindon.bbddconection;

import com.example.dindon.dtofront.Pisos;
import com.example.dindon.helpers.CONSTANTS;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PisosRepository {
    private ApiService apiService;

    public PisosRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CONSTANTS.URL_CONEXION )
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public void obtenerPisos(Callback<List<Pisos>>callback) {
        Call<List<Pisos>> call = apiService.obtenerPisos();
        call.enqueue(callback);
    }
}