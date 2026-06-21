package com.licoreria.dominio.catalogo;


public class Impuesto {
    private Integer id;
    private String nombre; // Ej. "IGV"
    private double porcentaje; // Ej. 18.0000
    private TipoImpuesto tipo; // ENUM: PORCENTAJE, MONTO_FIJO
    private Boolean activo;

    public Impuesto() {
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

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public TipoImpuesto getTipo() {
        return tipo;
    }

    public void setTipo(TipoImpuesto tipo) {
        this.tipo = tipo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }


}