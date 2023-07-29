package com.example.dindonback.DTOs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ImageMetadataTest {

    private ImageMetadata imageMetadata;

    @BeforeEach
    void setUp() {
        imageMetadata = new ImageMetadata("1", "testRuta");
    }

    @Test
    void getIdTest() {
        assertEquals("1", imageMetadata.getId());
    }

    @Test
    void setIdTest() {
        imageMetadata.setId("2");
        assertEquals("2", imageMetadata.getId());
    }

    @Test
    void getRutaTest() {
        assertEquals("testRuta", imageMetadata.getRuta());
    }

    @Test
    void setRutaTest() {
        imageMetadata.setRuta("newTestRuta");
        assertEquals("newTestRuta", imageMetadata.getRuta());
    }
}
