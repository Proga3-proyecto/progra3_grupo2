package com.licoreria.dominio.catalogo;

public class AlcoholImpuesto {
    private Integer id;
    private Integer minimo;
    private Integer maximo;
    private Integer porcentajePrecio;
    private double valor;


    public AlcoholImpuesto() {
    }




    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Integer getPorcentajePrecio() {
        return porcentajePrecio;
    }

    public void setPorcentajePrecio(Integer porcentajePrecio) {
        this.porcentajePrecio = porcentajePrecio;
    }

    public Integer getMaximo() {
        return maximo;
    }

    public void setMaximo(Integer maximo) {
        this.maximo = maximo;
    }

    public Integer getMinimo() {
        return minimo;
    }

    public void setMinimo(Integer minimo) {
        this.minimo = minimo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}