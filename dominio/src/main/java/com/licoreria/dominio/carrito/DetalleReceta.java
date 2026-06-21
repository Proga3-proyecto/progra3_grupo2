package com.licoreria.dominio.carrito;

import com.licoreria.dominio.catalogo.Receta;
import com.licoreria.dominio.usuarios.Cliente;


public class DetalleReceta {
    private Receta receta;
    private Cliente clienteCarrito;
    private Integer cantidad;
    private double descuentoTotal;
    private double montoTotal;

    public DetalleReceta() {
    }


    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }

    public Cliente getClienteCarrito() {
        return clienteCarrito;
    }

    public void setClienteCarrito(Cliente clienteCarrito) {
        this.clienteCarrito = clienteCarrito;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public double getDescuentoTotal() {
        return descuentoTotal;
    }

    public void setDescuentoTotal(double descuentoTotal) {
        this.descuentoTotal = descuentoTotal;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }
}