package com.example.dindon.dtofront;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pisos implements Serializable {

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
    private List<String> serviciosDisponibles;

    private List<ImageMetadata> images;

    public Pisos() {
        this.images = new ArrayList<>();
        this.serviciosDisponibles = new ArrayList<>();
    }

    public List<ImageMetadata> getImagenes() {
        return images;
    }

    public void setImagenes(List<ImageMetadata> imagenes) {
        this.images = imagenes;
    }

    public Pisos(String id, String nombre, String descripcion, String propietario, Double precio,
                 Integer habitaciones, Integer banos, String direccion, Double latitud,
                 Double longitud, List<String> serviciosDisponibles, List<ImageMetadata> imagenes) {
        this.id =id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.propietario = propietario;
        this.precio = precio;
        this.habitaciones = habitaciones;
        this.banos = banos;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.serviciosDisponibles = serviciosDisponibles;
        this.images = imagenes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
