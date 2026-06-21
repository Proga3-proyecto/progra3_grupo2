package com.licoreria.dominio.catalogo;

import java.util.ArrayList;
import java.util.List;

public class Producto {
    private Integer id;
    private String nombre;
    private double precio;
    private double precioFinal;
    private Integer stock;
    private double descuento;
    private double volumenLitros;
    private double porcentajeAlcohol;
    private Marca marca;
    private Impuesto impuestoBase;
    private AlcoholImpuesto impuestoAlcohol;
    private List<Categoria> categorias;
    private List<Imagen> imagenes;

    public Producto() {
        categorias = new ArrayList<>();
        imagenes = new ArrayList<>();
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getVolumenLitros() {
        return volumenLitros;
    }

    public void setVolumenLitros(double volumenLitros) {
        this.volumenLitros = volumenLitros;
    }

    public double getPorcentajeAlcohol() {
        return porcentajeAlcohol;
    }

    public void setPorcentajeAlcohol(double porcentajeAlcohol) {
        this.porcentajeAlcohol = porcentajeAlcohol;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Impuesto getImpuestoBase() {
        return impuestoBase;
    }

    public void setImpuestoBase(Impuesto impuestoBase) {
        this.impuestoBase = impuestoBase;
    }

    public AlcoholImpuesto getImpuestoAlcohol() {
        return impuestoAlcohol;
    }

    public void setImpuestoAlcohol(AlcoholImpuesto impuestoAlcohol) {
        this.impuestoAlcohol = impuestoAlcohol;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public List<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Imagen> imagenes) {
        this.imagenes = imagenes;
    }


}