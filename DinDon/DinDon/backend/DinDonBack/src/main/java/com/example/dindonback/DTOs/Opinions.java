package com.example.dindonback.DTOs;
public class Opinions {
    private String idusuario;
    private String idpiso;
    private String opinion;
    private float valoracion;


    public Opinions(String idusuario, String idpiso, String opinion, float valoracion) {
        this.idusuario = idusuario;
        this.idpiso = idpiso;
        this.opinion = opinion;
        this.valoracion = valoracion;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public String getIdpiso() {
        return idpiso;
    }

    public void setIdpiso(String idpiso) {
        this.idpiso = idpiso;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public float getValoracion() {
        return valoracion;
    }

    public void setValoracion(float valoracion) {
        this.valoracion = valoracion;
    }


}
