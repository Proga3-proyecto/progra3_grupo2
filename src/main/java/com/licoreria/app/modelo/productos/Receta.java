package com.licoreria.app.modelo.productos;

import java.util.List;

public class Receta {
    private int idReceta;
    private String nombre;
    private List<ElementoReceta> productos;
    private String descripcion;
    private String imageSRC;
    
    public int getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(int idReceta) {
        this.idReceta = idReceta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<ElementoReceta> getProductos() {
        return productos;
    }

    public void setProductos(List<ElementoReceta> productos) {
        this.productos = productos;
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
