package com.example.dindon.dtofront;


import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String email;
    private String password;
    private String nombre;
    private List<String> friends;

    private List<String> preferencias;

    private List<String> friendsRequests;

    public User() {
        this.friends = new ArrayList<>();
        this.friendsRequests = new ArrayList<>();
        this.preferencias = new ArrayList<>();
    }

    private String tipoacceso;

    public List<String> getPreferencias() {
        return preferencias;
    }

    public void setPreferencias(List<String> preferencias) {
        this.preferencias = preferencias;
    }

    public String getTipoacceso() {
        return tipoacceso;
    }

    public void setTipoacceso(String tipoacceso) {
        this.tipoacceso = tipoacceso;
    }

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
        return id;
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


    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<String> getFriendsRequest() {
        return friendsRequests;
    }

    public void setFriendsRequest(List<String> friendsRequests) {
        this.friendsRequests = friendsRequests;
    }

    public String getPassword() {
        return password;
    }
}
