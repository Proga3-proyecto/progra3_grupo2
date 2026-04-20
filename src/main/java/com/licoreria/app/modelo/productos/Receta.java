package com.licoreria.app.modelo.productos;

import java.util.List;

public class Receta {
    private long id;
    private String nombre;
    private List<ElementoReceta> elementos;
    private String descripcion;
    private String imageSRC;

    public Receta(){}

    public Receta(String nombre, String descripcion, String imageSRC, List<ElementoReceta> elementos) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imageSRC = imageSRC;
        this.elementos = elementos;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<ElementoReceta> getElementos() {
        return elementos;
    }

    public void setElementos(List<ElementoReceta> elementos) {
        this.elementos = elementos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImageSRC() {
        return imageSRC;
    }

    public void setImageSRC(String imageSRC) {
        this.imageSRC = imageSRC;
    }


}
