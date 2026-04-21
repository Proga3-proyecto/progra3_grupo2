package com.licoreria.app.dominio.productos;


public class ElementoReceta {
    private Producto producto;
    private long id;
    private double cantidad;

    public ElementoReceta(Producto producto, double cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
