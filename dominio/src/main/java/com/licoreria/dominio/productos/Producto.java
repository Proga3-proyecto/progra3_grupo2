package com.licoreria.dominio.productos;

import java.util.List;

public class Producto {
    private long id;
    private String nombre;
    private double precio;
    private int stock;
    private double descuento;
    private String imagenURL;
    private Double volumenLitros;
    private Double porcentajeAlcohol;
    private Categoria categoria;
    private List<Impuesto> impuestos;
    private Marca marca;

    public Producto() {
    }

    public Producto(String nombre, String imagenURL, double precio, int stock, double descuento) {
        this.nombre = nombre;
        this.imagenURL = imagenURL;
        this.precio = precio;
        this.stock = stock;
        this.descuento = descuento;
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

    public Double getVolumenLitros() {
        return volumenLitros;
    }

    public void setVolumenLitros(Double volumenLitros) {
        this.volumenLitros = volumenLitros;
    }

    public Double getPorcentajeAlcohol() {
        return porcentajeAlcohol;
    }

    public void setPorcentajeAlcohol(Double porcentajeAlcohol) {
        this.porcentajeAlcohol = porcentajeAlcohol;
    }

    public List<Impuesto> getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(List<Impuesto> impuestos) {
        this.impuestos = impuestos;
    }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public Marca getMarca() { return marca; }
    public void setMarca(Marca marca) { this.marca = marca; }
}