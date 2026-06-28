package com.licoreria.dominio.Snapshots;

import com.licoreria.dominio.catalogo.ElementoReceta;

public class RecetaSnapshotElemento {
    private ProductoSnapshot productoSnapshot;
    private double cantidad;

    public RecetaSnapshotElemento() {
    }

    public RecetaSnapshotElemento(ElementoReceta elementoVivo) {
        if (elementoVivo != null) {
            this.cantidad = elementoVivo.getCantidad();
            if (elementoVivo.getProducto() != null) {
                this.productoSnapshot = new ProductoSnapshot(elementoVivo.getProducto());
            }
        }
    }

    public ProductoSnapshot getProductoSnapshot() {
        return productoSnapshot;
    }

    public void setProductoSnapshot(ProductoSnapshot productoSnapshot) {
        this.productoSnapshot = productoSnapshot;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }
}