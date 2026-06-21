package com.licoreria.dominio.catalogo;

public class Categoria {

    private Integer id;
    private String nombre;

    public Categoria() {
    }

    public Categoria(String nombre) {
        this.nombre = nombre;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


}