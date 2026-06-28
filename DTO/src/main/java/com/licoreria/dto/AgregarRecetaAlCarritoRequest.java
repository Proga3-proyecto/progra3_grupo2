package com.licoreria.dto;

public class AgregarRecetaAlCarritoRequest {
    private int idReceta;
    private int cantidad;

    public AgregarRecetaAlCarritoRequest() {
    }

    public AgregarRecetaAlCarritoRequest(int idReceta, int cantidad) {
        this.idReceta = idReceta;
        this.cantidad = cantidad;
    }

    public int getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(int idReceta) {
        this.idReceta = idReceta;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
