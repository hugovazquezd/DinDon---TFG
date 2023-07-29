package com.example.dindonback.DTOs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoordenadasTest {

    private Coordenadas coordenadas;

    @BeforeEach
    void setUp() {
        coordenadas = new Coordenadas(0.0f, 0.0f);
    }

    @Test
    void testGetLatitud() {
        assertEquals(0.0f, coordenadas.getLatitud());
    }

    @Test
    void testSetLatitud() {
        coordenadas.setLatitud(1.0f);
        assertEquals(1.0f, coordenadas.getLatitud());
    }

    @Test
    void testGetLongitud() {
        assertEquals(0.0f, coordenadas.getLongitud());
    }

    @Test
    void testSetLongitud() {
        coordenadas.setLongitud(1.0f);
        assertEquals(1.0f, coordenadas.getLongitud());
    }
}
