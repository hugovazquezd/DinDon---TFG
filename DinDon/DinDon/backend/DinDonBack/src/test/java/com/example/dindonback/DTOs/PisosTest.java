package com.example.dindonback.DTOs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PisosTest {

    private Pisos pisos;
    private final List<String> serviciosDisponibles = Arrays.asList("servicio1", "servicio2");

    @BeforeEach
    public void setup() {
        pisos = new Pisos();
        pisos.setid("id_piso");
        pisos.setNombre("nombre_piso");
        pisos.setDescripcion("descripcion_piso");
        pisos.setPropietario("propietario_piso");
        pisos.setPrecio(100.0);
        pisos.setHabitaciones(3);
        pisos.setBanos(2);
        pisos.setDireccion("direccion_piso");
        pisos.setLatitud(40.123456);
        pisos.setLongitud(-3.123456);
        pisos.setServiciosDisponibles(serviciosDisponibles);
    }

    @Test
    void testConstructor() {
        Pisos piso = new Pisos();
        assertTrue(piso.getImages().isEmpty());
    }

    @Test
    void testGetid() {
        assertEquals("id_piso", pisos.getid());
    }

    @Test
    void testGetServiciosDisponibles() {
        assertEquals(serviciosDisponibles, pisos.getServiciosDisponibles());
    }


    @Test
    void testSetid() {
        pisos.setid("new_id_piso");
        assertEquals("new_id_piso", pisos.getid());
    }

    @Test
    void testGetNombre() {
        assertEquals("nombre_piso", pisos.getNombre());
    }

    @Test
    void testSetNombre() {
        pisos.setNombre("new_nombre_piso");
        assertEquals("new_nombre_piso", pisos.getNombre());
    }

    @Test
    void testGetDescripcion() {
        assertEquals("descripcion_piso", pisos.getDescripcion());
    }

    @Test
    void testSetDescripcion() {
        pisos.setDescripcion("new_descripcion_piso");
        assertEquals("new_descripcion_piso", pisos.getDescripcion());
    }

    @Test
    void testGetPropietario() {
        assertEquals("propietario_piso", pisos.getPropietario());
    }

    @Test
    void testSetPropietario() {
        pisos.setPropietario("new_propietario_piso");
        assertEquals("new_propietario_piso", pisos.getPropietario());
    }

    @Test
    void testGetPrecio() {
        assertEquals(100.0, pisos.getPrecio());
    }

    @Test
    void testSetPrecio() {
        pisos.setPrecio(200.0);
        assertEquals(200.0, pisos.getPrecio());
    }

    @Test
    void testGetHabitaciones() {
        assertEquals(3, pisos.getHabitaciones());
    }

    @Test
    void testSetHabitaciones() {
        pisos.setHabitaciones(4);
        assertEquals(4, pisos.getHabitaciones());
    }

    @Test
    void testGetBanos() {
        assertEquals(2, pisos.getBanos());
    }

    @Test
    void testSetBanos() {
        pisos.setBanos(3);
        assertEquals(3, pisos.getBanos());
    }

    @Test
    void testGetDireccion() {
        assertEquals("direccion_piso", pisos.getDireccion());
    }

    @Test
    void testSetDireccion() {
        pisos.setDireccion("new_direccion_piso");
        assertEquals("new_direccion_piso", pisos.getDireccion());
    }

    @Test
    void testGetLatitud() {
        assertEquals(40.123456, pisos.getLatitud());
    }

    @Test
    void testSetLatitud() {
        pisos.setLatitud(41.123456);
        assertEquals(41.123456, pisos.getLatitud());
    }

    @Test
    void testGetLongitud() {
        assertEquals(-3.123456, pisos.getLongitud());
    }

    @Test
    void testSetLongitud() {
        pisos.setLongitud(-4.123456);
        assertEquals(-4.123456, pisos.getLongitud());
    }

    @Test
    void testSetServiciosDisponibles() {
        List<String> newServiciosDisponibles = Arrays.asList("servicio3", "servicio4");
        pisos.setServiciosDisponibles(newServiciosDisponibles);
        assertEquals(newServiciosDisponibles, pisos.getServiciosDisponibles());
    }

    @Test
    void testSetImages() {
        // Crear una lista de imágenes de prueba
        List<ImageMetadata> images = new ArrayList<>();
        images.add(new ImageMetadata("1", "ruta1"));
        images.add(new ImageMetadata("2", "ruta2"));

        // Crear una instancia de ImageMetadata
        ImageMetadata imageMetadata = new ImageMetadata("originalId", "originalRuta");

        // Llamar al método setImages para establecer las imágenes
        pisos.setImages(images);

        // Verificar que las imágenes se hayan establecido correctamente
        assertEquals(images, pisos.getImages());
    }


}
