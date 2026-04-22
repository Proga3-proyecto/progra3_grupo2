package com.licoreria.dominio.productos;

public class Impuesto {
    private Long id;
    private String nombre;
    private Double valor;
    private TipoImpuesto tipo;
    private Boolean activo;

    public Impuesto() {}

    public Impuesto(String nombre, Double valor, TipoImpuesto tipo, Boolean activo) {
        this.nombre = nombre;
        this.valor = valor;
        this.tipo = tipo;
        this.activo = activo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
    public TipoImpuesto getTipo() { return tipo; }
    public void setTipo(TipoImpuesto tipo) { this.tipo = tipo; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}