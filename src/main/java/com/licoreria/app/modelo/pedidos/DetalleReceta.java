package com.licoreria.app.modelo.pedidos;

import com.licoreria.app.modelo.productos.Receta;

import java.util.List;

public class DetalleReceta extends DetallePedido {
    private Receta recetaBase;
    private List<DetalleElementoReceta> elementosDesglosados;

    public List<DetalleElementoReceta> getElementosDesglosados() {
        return elementosDesglosados;
    }

    public void setElementosDesglosados(List<DetalleElementoReceta> elementosDesglosados) {
        this.elementosDesglosados = elementosDesglosados;
    }

    public Receta getRecetaBase() {
        return recetaBase;
    }

    public void setRecetaBase(Receta recetaBase) {
        this.recetaBase = recetaBase;
    }

}
