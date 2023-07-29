package com.example.dindon.dtofront;

import org.junit.Test;

import static org.junit.Assert.*;

public class OpinionsTest {

    @Test
    public void testIdusuario() {
        String expectedIdusuario = "testId";
        Opinions opinions = new Opinions(expectedIdusuario, "testOpinion", 3.5f);
        assertEquals(expectedIdusuario, opinions.getIdusuario());

        String newIdusuario = "newTestId";
        opinions.setIdusuario(newIdusuario);
        assertEquals(newIdusuario, opinions.getIdusuario());
    }

    @Test
    public void testOpinion() {
        String expectedOpinion = "testOpinion";
        Opinions opinions = new Opinions("testId", expectedOpinion, 3.5f);
        assertEquals(expectedOpinion, opinions.getOpinion());

        String newOpinion = "newTestOpinion";
        opinions.setOpinion(newOpinion);
        assertEquals(newOpinion, opinions.getOpinion());
    }

    @Test
    public void testValoracion() {
        float expectedValoracion = 3.5f;
        Opinions opinions = new Opinions("testId", "testOpinion", expectedValoracion);
        assertEquals(expectedValoracion, opinions.getValoracion(), 0.001);

        float newValoracion = 4.0f;
        opinions.setValoracion(newValoracion);
        assertEquals(newValoracion, opinions.getValoracion(), 0.001);
    }
}
