package com.licoreria.dominio.Snapshots;

import com.licoreria.dominio.catalogo.ElementoReceta;

public class RecetaSnapshotElemento {
    private RecetaSnapshot recetaSnapshot;
    private ProductoSnapshot productoSnapshot;
    private double cantidad;

    public RecetaSnapshotElemento() {
    }

    public RecetaSnapshotElemento(ElementoReceta elementoVivo, RecetaSnapshot recetaPadre) {
        if (elementoVivo != null) {
            this.recetaSnapshot = recetaPadre;
            this.cantidad = elementoVivo.getCantidad();

            if (elementoVivo.getProducto() != null) {
                this.productoSnapshot = new ProductoSnapshot(elementoVivo.getProducto());
            }
        }
    }

    public RecetaSnapshot getRecetaSnapshot() {
        return recetaSnapshot;
    }

    public void setRecetaSnapshot(RecetaSnapshot recetaSnapshot) {
        this.recetaSnapshot = recetaSnapshot;
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