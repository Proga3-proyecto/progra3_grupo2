package com.licoreria.app.modelo.pedidos;

import com.licoreria.app.modelo.productos.ElementoReceta;

public class DetalleElementoReceta {
    private ElementoReceta elementoBase;
    private double cantidadEspecifica;
    
    public ElementoReceta getElementoBase() {
        return elementoBase;
    }

    public void setElementoBase(ElementoReceta elementoBase) {
        this.elementoBase = elementoBase;
    }

    public double getCantidadEspecifica() {
        return cantidadEspecifica;
    }

    public void setCantidadEspecifica(double cantidadEspecifica) {
        this.cantidadEspecifica = cantidadEspecifica;
    }

}
