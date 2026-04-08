package com.licoreria.app.modelo.productos;

import com.licoreria.app.modelo.pedidos.Pedido;

import java.util.List;

public class Producto {
    private int idProducto;
    private String  nombre;
    private double precio;
    private int stock;
    private double descuento;
    private String imagenURL;
    private boolean paraCombos;


    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public String getImagenURL() {
        return imagenURL;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }

    public boolean isParaCombos() {
        return paraCombos;
    }

    public void setParaCombos(boolean paraCombos) {
        this.paraCombos = paraCombos;
    }
}
