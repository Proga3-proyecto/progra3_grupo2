package com.licoreria.app.modelo.pedidos;

public abstract class DetallePedido {
    private double descuentoTotal;
    private double montoTotal;

    public double getDescuentoTotal() {
        return descuentoTotal;
    }

    public void setDescuentoTotal(double descuentoTotal) {
        this.descuentoTotal = descuentoTotal;
    }
}
