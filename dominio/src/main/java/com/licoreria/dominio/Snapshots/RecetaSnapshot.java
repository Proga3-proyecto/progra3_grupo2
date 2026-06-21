package com.licoreria.dominio.Snapshots;

import com.licoreria.dominio.catalogo.ElementoReceta;
import com.licoreria.dominio.catalogo.Imagen;
import com.licoreria.dominio.catalogo.Receta;

import java.util.ArrayList;
import java.util.List;

public class RecetaSnapshot {
    private Integer id;
    private Receta recetaOriginal;
    private String nombre;
    private String descripcion;
    private String instrucciones;
    private double precioHistorico;
    private double precioFinalHistorico;
    private List<Imagen> imagenesHistoricas;
    private List<RecetaSnapshotElemento> elementosHistoricos;

    public RecetaSnapshot() {
        this.imagenesHistoricas = new ArrayList<>();
        this.elementosHistoricos = new ArrayList<>();
    }

    public RecetaSnapshot(Receta receta) {
        this();

        if (receta != null) {
            this.recetaOriginal = receta;
            this.nombre = receta.getNombre();
            this.descripcion = receta.getDescripcion();
            this.instrucciones = receta.getInstrucciones();
            this.precioHistorico = receta.getPrecio();
            this.precioFinalHistorico = receta.getPrecioFinal();

            if (receta.getImagenes() != null) {
                this.imagenesHistoricas.addAll(receta.getImagenes());
            }

            if (receta.getElementos() != null) {
                for (ElementoReceta elementoVivo : receta.getElementos()) {
                    RecetaSnapshotElemento elementoHistorico = new RecetaSnapshotElemento(elementoVivo, this);
                    this.elementosHistoricos.add(elementoHistorico);
                }
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Receta getRecetaOriginal() {
        return recetaOriginal;
    }

    public void setRecetaOriginal(Receta recetaOriginal) {
        this.recetaOriginal = recetaOriginal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public double getPrecioHistorico() {
        return precioHistorico;
    }

    public void setPrecioHistorico(double precioHistorico) {
        this.precioHistorico = precioHistorico;
    }

    public double getPrecioFinalHistorico() {
        return precioFinalHistorico;
    }

    public void setPrecioFinalHistorico(double precioFinalHistorico) {
        this.precioFinalHistorico = precioFinalHistorico;
    }

    public List<Imagen> getImagenesHistoricas() {
        return imagenesHistoricas;
    }

    public void setImagenesHistoricas(List<Imagen> imagenesHistoricas) {
        this.imagenesHistoricas = imagenesHistoricas;
    }

    public List<RecetaSnapshotElemento> getElementosHistoricos() {
        return elementosHistoricos;
    }

    public void setElementosHistoricos(List<RecetaSnapshotElemento> elementosHistoricos) {
        this.elementosHistoricos = elementosHistoricos;
    }
}