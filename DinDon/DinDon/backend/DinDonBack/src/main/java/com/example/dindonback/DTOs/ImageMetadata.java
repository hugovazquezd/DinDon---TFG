package com.example.dindonback.DTOs;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "pisos")
public class ImageMetadata {
    @Field("id")
    private String id;
    @Field("ruta")
    private String ruta;

    public ImageMetadata(String id, String ruta) {
        this.id = id;
        this.ruta = ruta;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

}
