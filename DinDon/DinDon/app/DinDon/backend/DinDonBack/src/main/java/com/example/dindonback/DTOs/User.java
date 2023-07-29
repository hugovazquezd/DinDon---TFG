package com.example.dindonback.DTOs;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "users")
public class User {

    @Id
    private String _id;
    private String email;
    private String password;
    private List<String> friends;
    private String TipoAcceso;

    @Field("name")
    private String nombre;

    @DBRef
    private Set<Role> roles = new HashSet<>();


    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    // Constructores, getters y setters

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(){

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

    public String get_id() {
        return _id;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void set_id(String _id) {
        this._id = _id;
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
        return TipoAcceso;
    }

    public void setTipoAcceso(String tipoAcceso) {
        TipoAcceso = tipoAcceso;
    }
}
