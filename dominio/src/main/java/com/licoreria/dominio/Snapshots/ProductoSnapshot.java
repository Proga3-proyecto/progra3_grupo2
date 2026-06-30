package com.licoreria.dominio.Snapshots;

import com.licoreria.dominio.catalogo.Categoria;
import com.licoreria.dominio.catalogo.Imagen;
import com.licoreria.dominio.catalogo.Producto;
import com.licoreria.dominio.catalogo.TipoImpuesto;

import java.util.ArrayList;
import java.util.List;

public class ProductoSnapshot {
    private Integer id;
    private Producto productoOriginal;
    private String nombre;
    private double precioVenta;
    private double precioFinalVenta;
    private double descuentoApplied;
    private double volumenLitros;
    private double porcentajeAlcohol;
    private String nombreMarca;

    private String nombreImpuesto;
    private double porcentajeImpuesto;
    private TipoImpuesto tipoImpuesto;


    private Double valorImpuestoAlcoholHistorico;
    private Imagen imagen;

    private List<String> categoriasHistoricas;



    public Imagen getImagen() {
        return imagen;
    }

    public void setImagen(Imagen imagen) {
        this.imagen = imagen;
    }



    public ProductoSnapshot(){
    }

    public ProductoSnapshot(Producto producto) {
        this();

        if (producto != null) {
            this.productoOriginal = producto;
            this.nombre = producto.getNombre();
            this.precioVenta = producto.getPrecio();
            this.precioFinalVenta = producto.getPrecioFinal();
            this.descuentoApplied = producto.getDescuento();
            this.volumenLitros = producto.getVolumenLitros();
            this.porcentajeAlcohol = producto.getPorcentajeAlcohol();

            if (producto.getMarca() != null) {
                this.nombreMarca = producto.getMarca().getNombre();
            }

            if (producto.getImpuestoBase() != null) {
                this.nombreImpuesto = producto.getImpuestoBase().getNombre();
                this.porcentajeImpuesto = producto.getImpuestoBase().getPorcentaje();
                this.tipoImpuesto = producto.getImpuestoBase().getTipo();
            }

            if (producto.getImpuestoAlcohol() != null) {
                this.valorImpuestoAlcoholHistorico = producto.getImpuestoAlcohol().getValor();
            } else {
                this.valorImpuestoAlcoholHistorico = 0.0;
            }

            if(producto.getCategorias() != null) {
                this.categoriasHistoricas = producto.getCategorias().stream().map(Categoria::getNombre).toList();
            }

            if(producto.getImagenes() != null){
                imagen = producto.getImagenes().getFirst();
            }

        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Producto getProductoOriginal() {
        return productoOriginal;
    }

    public void setProductoOriginal(Producto productoOriginal) {
        this.productoOriginal = productoOriginal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public double getPrecioFinalVenta() {
        return precioFinalVenta;
    }

    public void setPrecioFinalVenta(double precioFinalVenta) {
        this.precioFinalVenta = precioFinalVenta;
    }

    public double getDescuentoApplied() {
        return descuentoApplied;
    }

    public void setDescuentoApplied(double descuentoApplied) {
        this.descuentoApplied = descuentoApplied;
    }

    public double getVolumenLitros() {
        return volumenLitros;
    }

    public void setVolumenLitros(double volumenLitros) {
        this.volumenLitros = volumenLitros;
    }

    public double getPorcentajeAlcohol() {
        return porcentajeAlcohol;
    }

    public void setPorcentajeAlcohol(double porcentajeAlcohol) {
        this.porcentajeAlcohol = porcentajeAlcohol;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    public String getNombreImpuesto() {
        return nombreImpuesto;
    }

    public void setNombreImpuesto(String nombreImpuesto) {
        this.nombreImpuesto = nombreImpuesto;
    }

    public double getPorcentajeImpuesto() {
        return porcentajeImpuesto;
    }

    public void setPorcentajeImpuesto(double porcentajeImpuesto) {
        this.porcentajeImpuesto = porcentajeImpuesto;
    }

    public TipoImpuesto getTipoImpuesto() {
        return tipoImpuesto;
    }

    public void setTipoImpuesto(TipoImpuesto tipoImpuesto) {
        this.tipoImpuesto = tipoImpuesto;
    }



    public Double getValorImpuestoAlcoholHistorico() {
        return valorImpuestoAlcoholHistorico;
    }

    public void setValorImpuestoAlcoholHistorico(Double valorImpuestoAlcoholHistorico) {
        this.valorImpuestoAlcoholHistorico = valorImpuestoAlcoholHistorico;
    }
    public List<String> getCategoriasHistoricas() {
        return categoriasHistoricas;
    }

    public void setCategoriasHistoricas(List<String> categoriasHistoricas) {
        this.categoriasHistoricas = categoriasHistoricas;
    }



}