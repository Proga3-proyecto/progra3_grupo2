package com.licoreria.dominio.catalogo;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ElementoReceta {
    private Integer id;
    @JsonIgnore
    private Receta receta;
    private Producto producto;
    private double cantidad;
    public ElementoReceta() {
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }
}