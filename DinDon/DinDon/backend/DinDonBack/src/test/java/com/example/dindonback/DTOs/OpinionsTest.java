package com.example.dindonback.DTOs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

 class OpinionsTest {

    private Opinions opinions;

    @BeforeEach
    public void setup() {
        opinions = new Opinions("id_usuario", "id_piso", "opinion", 4.5f);
    }

    @Test
     void testGetIdusuario() {
        assertEquals("id_usuario", opinions.getIdusuario());
    }

    @Test
     void testSetIdusuario() {
        opinions.setIdusuario("new_id_usuario");
        assertEquals("new_id_usuario", opinions.getIdusuario());
    }

    @Test
     void testGetIdpiso() {
        assertEquals("id_piso", opinions.getIdpiso());
    }

    @Test
     void testSetIdpiso() {
        opinions.setIdpiso("new_id_piso");
        assertEquals("new_id_piso", opinions.getIdpiso());
    }

    @Test
     void testGetOpinion() {
        assertEquals("opinion", opinions.getOpinion());
    }

    @Test
     void testSetOpinion() {
        opinions.setOpinion("new_opinion");
        assertEquals("new_opinion", opinions.getOpinion());
    }

    @Test
     void testGetValoracion() {
        assertEquals(4.5f, opinions.getValoracion());
    }

    @Test
     void testSetValoracion() {
        opinions.setValoracion(3.5f);
        assertEquals(3.5f, opinions.getValoracion());
    }
}
