package com.example.dindon.bbddconection;

import com.example.dindon.dtofront.Pisos;

import java.util.List;

public interface PisosCallback {
    void onPisosObtenidos(List<Pisos> pisos);
    void onError(Throwable t);
}
