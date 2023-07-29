package com.example.dindon.dtofront;

public class ImageMetadata  implements java.io.Serializable{
    private String id;
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
// Getters y setters
}
