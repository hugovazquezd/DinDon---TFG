package com.example.dindonback.bbdd;

import com.example.dindonback.DTOs.Opinions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

 class OpinionControllerTest {

    @Mock
    private OpinionRepository opinionRepository;

    @InjectMocks
    private OpinionController opinionController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOpinion() {
        // Creamos un objeto de tipo Opinions
        Opinions opinion = new Opinions("usuario", "piso", "opinion", 5);

        // Hacemos que el método save del repositorio retorne el mismo objeto que le pasamos
        when(opinionRepository.save(any(Opinions.class))).thenReturn(opinion);

        // Llamamos al método a probar y comparamos el resultado
        Opinions result = opinionController.createOpinion(opinion);

        assertEquals(opinion, result, "The returned opinion should match the created one");
    }
}
