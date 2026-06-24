package com.licoreria.dominio.catalogo;

import java.util.ArrayList;
import java.util.List;

public class Receta {
    private Integer id;
    private String nombre;
    private String descripcion;
    private String instrucciones;
    private double precio;
    private double precioFinal;
    private double descuento;
    private List<ElementoReceta> elementos;
    private List<Imagen> imagenes;


    private List<Categoria> categorias;

    public Receta() {
        imagenes = new ArrayList<>();
        elementos = new ArrayList<>();
    }

    public Receta(String nombre, String descripcion, String instrucciones, double precio, double precioFinal, double descuento, List<ElementoReceta> elementos, List<Imagen> imagenes) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.instrucciones = instrucciones;
        this.precio = precio;
        this.precioFinal = precioFinal;
        this.descuento = descuento;
        this.elementos = elementos;
        this.imagenes = imagenes;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
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

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public List<ElementoReceta> getElementos() {
        return elementos;
    }

    public void setElementos(List<ElementoReceta> elementos) {
        this.elementos = elementos;
    }

    public List<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Imagen> imagenes) {
        this.imagenes = imagenes;
    }



}