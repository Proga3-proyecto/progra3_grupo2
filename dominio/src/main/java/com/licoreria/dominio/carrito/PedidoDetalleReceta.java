package com.licoreria.dominio.carrito;

import com.licoreria.dominio.Snapshots.RecetaSnapshot;


public class PedidoDetalleReceta {
    private Integer id;
    private Pedido pedido;
    private RecetaSnapshot recetaSnapshot;
    private Integer cantidad;
    private double descuentoHistorico;

    public PedidoDetalleReceta() {
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

    public RecetaSnapshot getRecetaSnapshot() {
        return recetaSnapshot;
    }

    public void setRecetaSnapshot(RecetaSnapshot recetaSnapshot) {
        this.recetaSnapshot = recetaSnapshot;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public double getDescuentoHistorico() {
        return descuentoHistorico;
    }

    public void setDescuentoHistorico(double descuentoHistorico) {
        this.descuentoHistorico = descuentoHistorico;
    }


}