package com.licoreria.dominio.carrito;

import com.licoreria.dominio.Snapshots.ProductoSnapshot;

public class PedidoDetalleProducto {
    private Integer id;
    private Pedido pedido;
    private ProductoSnapshot productoSnapshot;
    private Integer cantidad;

    public PedidoDetalleProducto() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public ProductoSnapshot getProductoSnapshot() {
        return productoSnapshot;
    }

    public void setProductoSnapshot(ProductoSnapshot productoSnapshot) {
        this.productoSnapshot = productoSnapshot;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}