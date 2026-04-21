package com.licoreria.app.dominio.pedidos;


import com.licoreria.app.dominio.productos.ElementoReceta;

public class DetalleElementoReceta {
    private long id;
    private ElementoReceta elementoBase;
    private int cantidadEspecifica;

    public  DetalleElementoReceta(ElementoReceta elemento){
        this.elementoBase = elemento;
    }
    
    public ElementoReceta getElementoBase() {
        return elementoBase;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
