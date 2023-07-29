package com.example.dindon.dtofront;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PisosTest {

    @Test
    public void testGetImagenes() {
        List<ImageMetadata> expectedImages = new ArrayList<>();
        expectedImages.add(new ImageMetadata("1", "path/to/image1.jpg"));
        expectedImages.add(new ImageMetadata("2", "path/to/image2.jpg"));

        Pisos pisos = new Pisos();
        pisos.setImagenes(expectedImages);

        List<ImageMetadata> actualImages = pisos.getImagenes();

        assertEquals(expectedImages, actualImages);
    }

    @Test
    public void testSetImagenes() {
        List<ImageMetadata> images = new ArrayList<>();
        images.add(new ImageMetadata("1", "path/to/image1.jpg"));
        images.add(new ImageMetadata("2", "path/to/image2.jpg"));

        Pisos pisos = new Pisos();
        pisos.setImagenes(images);

        List<ImageMetadata> actualImages = pisos.getImagenes();

        assertEquals(images, actualImages);
    }

    @Test
    public void testConstructor() {
        String id = "123";
        String nombre = "Piso Ejemplo";
        String descripcion = "Este es un piso de ejemplo";
        String propietario = "John Doe";
        Double precio = 1000.0;
        Integer habitaciones = 2;
        Integer banos = 1;
        String direccion = "Calle Principal 123";
        Double latitud = 40.12345;
        Double longitud = -3.67890;
        List<String> servicios = Arrays.asList("WiFi", "Piscina", "Gimnasio");
        List<ImageMetadata> images = new ArrayList<>();
        images.add(new ImageMetadata("1", "path/to/image1.jpg"));
        images.add(new ImageMetadata("2", "path/to/image2.jpg"));

        Pisos pisos = new Pisos(id, nombre, descripcion, propietario, precio, habitaciones, banos,
                direccion, latitud, longitud, servicios, images);

        assertEquals(id, pisos.getId());
        assertEquals(nombre, pisos.getNombre());
        assertEquals(descripcion, pisos.getDescripcion());
        assertEquals(propietario, pisos.getPropietario());
        assertEquals(precio, pisos.getPrecio());
        assertEquals(habitaciones, pisos.getHabitaciones());
        assertEquals(banos, pisos.getBanos());
        assertEquals(direccion, pisos.getDireccion());
        assertEquals(latitud, pisos.getLatitud());
        assertEquals(longitud, pisos.getLongitud());
        assertEquals(servicios, pisos.getServiciosDisponibles());
        assertEquals(images, pisos.getImagenes());
    }
}
