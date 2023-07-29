package com.example.dindon.DTOFront;


import java.util.List;

public class User {
    private String _id;
    private String email;
    private String password;
    private String nombre;
    private List<String> friends;

    private boolean isArrendador;

    public String getEmail() {
        return email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return _id;
    }

    public List<String> getFriends() {
        return friends;
    }

    public boolean getIsArrendador() {
        return isArrendador;
    }

    public void setIsArrendador(boolean isArrendador) {
        this.isArrendador = isArrendador;
    }
}
