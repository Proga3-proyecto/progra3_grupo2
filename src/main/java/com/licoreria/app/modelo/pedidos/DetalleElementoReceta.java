package com.licoreria.app.modelo.pedidos;

import com.licoreria.app.modelo.productos.ElementoReceta;

public class DetalleElementoReceta {
    private ElementoReceta elementoBase;
    private int cantidadEspecifica;

    public  DetalleElementoReceta(ElementoReceta elemento){
        this.elementoBase = elemento;
    }
    
    public ElementoReceta getElementoBase() {
        return elementoBase;
    }

    public void setElementoBase(ElementoReceta elementoBase) {
        this.elementoBase = elementoBase;
    }

    public int getCantidadEspecifica() {
        return cantidadEspecifica;
    }

    public void setCantidadEspecifica(int cantidadEspecifica) {
        this.cantidadEspecifica = cantidadEspecifica;
    }

}
