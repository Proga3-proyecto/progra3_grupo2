package com.licoreria.dominio.catalogo;

public class Marca {
    private Integer id;
    private String nombre;

    public Marca() {
    }

    public Marca(String nombre) {
        this.nombre = nombre;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}