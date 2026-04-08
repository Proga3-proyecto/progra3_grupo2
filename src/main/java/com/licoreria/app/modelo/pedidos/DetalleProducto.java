package com.licoreria.app.modelo.pedidos;

import com.licoreria.app.modelo.productos.Producto;

public class DetalleProducto extends DetallePedido{
    private Producto producto;
    private int cantidad;

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

}
