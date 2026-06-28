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
    private double precioHistorico;
    private double precioFinalHistorico;
    private Imagen imagen;
    private List<RecetaSnapshotElemento> elementosHistoricos;

    public RecetaSnapshot() {
        this.elementosHistoricos = new ArrayList<>();
    }

    public RecetaSnapshot(Receta receta) {
        this();

        if (receta != null) {
            this.recetaOriginal = receta;
            this.nombre = receta.getNombre();
            this.precioHistorico = receta.getPrecio();
            this.precioFinalHistorico = receta.getPrecioFinal();

            if (receta.getImagenes() != null) {
                imagen = receta.getImagenes().getFirst();
            }

            if (receta.getElementos() != null) {
                for (ElementoReceta elementoVivo : receta.getElementos()) {
                    RecetaSnapshotElemento elementoHistorico = new RecetaSnapshotElemento(elementoVivo);
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

    public Imagen getImagen() {
        return imagen;
    }

    public void setImagen(Imagen imagen) {
        this.imagen = imagen;
    }

    public List<RecetaSnapshotElemento> getElementosHistoricos() {
        return elementosHistoricos;
    }

    public void setElementosHistoricos(List<RecetaSnapshotElemento> elementosHistoricos) {
        this.elementosHistoricos = elementosHistoricos;
    }
}