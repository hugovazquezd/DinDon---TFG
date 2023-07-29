package com.example.dindonback.DTOs;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;


@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String email;
    private String password;
    private List<String> friends;
    private List<String> friendsRequests;

    private List<String> preferencias;

    @Field("tipoAcceso")
    private String tipoacceso;

    @Field("name")
    private String nombre;


    // Constructores, getters y setters

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.friends = new ArrayList<>();
        this.friendsRequests = new ArrayList<>();
        this.preferencias = new ArrayList<>();
    }


    public User(){
        this.friends = new ArrayList<>();
        this.friendsRequests = new ArrayList<>();
        this.preferencias = new ArrayList<>();
    }

    public List<String> getFriendsRequests() {
        return friendsRequests;
    }

    public List<String> getPreferencias() {
        return preferencias;
    }

    public void setPreferencias(List<String> preferencias) {
        this.preferencias = preferencias;
    }
    public void setFriendsRequests(List<String> friendsRequests) {
        this.friendsRequests = friendsRequests;
    }
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNombre() {
        return nombre;
    }

    public String getId() {
        return id;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoAcceso() {
        return tipoacceso;
    }

    public void setTipoAcceso(String tipoAcceso) {
        tipoacceso = tipoAcceso;
    }


}
