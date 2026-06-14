package com.licoreria.dominio.productos;

public class Marca {
    private Long id;
    private String nombre;

    public Marca() {}

    public Marca(String nombre) {
        this.nombre = nombre;
    }

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}