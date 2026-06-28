package com.licoreria.dto;

public class ActualizarCantidadRequest {
    private int cantidad;

    public ActualizarCantidadRequest() {
    }

    public ActualizarCantidadRequest(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
