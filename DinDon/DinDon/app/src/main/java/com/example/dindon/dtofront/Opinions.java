package com.example.dindon.dtofront;

public class Opinions {
    private String idusuario;
    private String opinion;
    private float valoracion;


    public Opinions(String idusuario, String opinion, float valoracion) {
        this.idusuario = idusuario;
        this.opinion = opinion;
        this.valoracion = valoracion;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
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
