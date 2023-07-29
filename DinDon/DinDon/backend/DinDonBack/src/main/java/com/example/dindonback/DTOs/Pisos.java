package com.example.dindonback.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "pisos")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pisos {
    @Id
    private String id;
    private String nombre;
    private String descripcion;
    private String propietario;
    private Double precio;
    private Integer habitaciones;
    private Integer banos;
    private String direccion;
    private Double latitud;
    private Double longitud;
    @Field("servicios_disponibles")
    private List<String> serviciosDisponibles;
    @Field("images")
    private ArrayList<ImageMetadata> images;

    public List<ImageMetadata> getImages() {
        return images;
    }

    public void setImages(List<ImageMetadata> images) {
        this.images = (ArrayList<ImageMetadata>) images;
    }

    public Pisos() {
        this.images = new ArrayList<>();
    }



    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(Integer habitaciones) {
        this.habitaciones = habitaciones;
    }

    public Integer getBanos() {
        return banos;
    }

    public void setBanos(Integer banos) {
        this.banos = banos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public List<String> getServiciosDisponibles() {
        return serviciosDisponibles;
    }

    public void setServiciosDisponibles(List<String> serviciosDisponibles) {
        this.serviciosDisponibles = serviciosDisponibles;
    }
}
