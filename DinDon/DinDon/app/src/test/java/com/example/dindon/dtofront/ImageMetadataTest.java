package com.example.dindon.dtofront;

import org.junit.Assert;
import org.junit.Test;

public class ImageMetadataTest {

    @Test
    public void testGetId() {
        String id = "123";
        String ruta = "path/to/image.jpg";
        ImageMetadata imageMetadata = new ImageMetadata(id, ruta);
        Assert.assertEquals(id, imageMetadata.getId());
    }

    @Test
    public void testSetId() {
        String id = "123";
        String ruta = "path/to/image.jpg";
        ImageMetadata imageMetadata = new ImageMetadata(null, null);
        imageMetadata.setId(id);
        Assert.assertEquals(id, imageMetadata.getId());
    }

    @Test
    public void testGetRuta() {
        String id = "123";
        String ruta = "path/to/image.jpg";
        ImageMetadata imageMetadata = new ImageMetadata(id, ruta);
        Assert.assertEquals(ruta, imageMetadata.getRuta());
    }

    @Test
    public void testSetRuta() {
        String id = "123";
        String ruta = "path/to/image.jpg";
        ImageMetadata imageMetadata = new ImageMetadata(null, null);
        imageMetadata.setRuta(ruta);
        Assert.assertEquals(ruta, imageMetadata.getRuta());
    }
}
