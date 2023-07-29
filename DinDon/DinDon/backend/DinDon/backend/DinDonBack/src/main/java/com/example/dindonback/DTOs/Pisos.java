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
    private String _id;
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

    public ArrayList<ImageMetadata> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageMetadata> images) {
        this.images = images;
    }

    public Pisos() {
        this.images = new ArrayList<>();
    }

    public Pisos(String _id, String nombre, String descripcion, String propietario, Double precio,
                 Integer habitaciones, Integer banos, String direccion, Double latitud,
                 Double longitud, List<String> serviciosDisponibles, ArrayList<ImageMetadata> images) {
        this._id = _id;
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
        this.images = images;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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
