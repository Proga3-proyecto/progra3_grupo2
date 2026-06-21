package com.licoreria.dominio.carrito;

import com.licoreria.dominio.usuarios.Cliente;

import java.time.LocalDate;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private Integer id;
    private Cliente cliente;
    private Date fechaPedido;
    private Date horaInicio;
    private Date horaFin;
    private double precioTotal;    // Suma de netos
    private double totalImpuestos; // Desglose acumulado de tasas
    private double precioDelivery;
    private double precioFinal;    // Monto final definitivo cobrado al usuario
    private EstadoPedido estado; // ENUM: PENDIENTE, EN_PREPARACION, EN_CAMINO, etc.
    private String direccionDestino;
    private List<PedidoDetalleProducto> detallesProductos;
    private List<PedidoDetalleReceta> detallesRecetas;


    public Pedido(){
        detallesProductos = new ArrayList<>();
        detallesRecetas = new ArrayList<>();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public double getTotalImpuestos() {
        return totalImpuestos;
    }

    public void setTotalImpuestos(double totalImpuestos) {
        this.totalImpuestos = totalImpuestos;
    }

    public double getPrecioDelivery() {
        return precioDelivery;
    }

    public void setPrecioDelivery(double precioDelivery) {
        this.precioDelivery = precioDelivery;
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
    }

    public List<PedidoDetalleProducto> getDetallesProductos() {
        return detallesProductos;
    }

    public void setDetallesProductos(List<PedidoDetalleProducto> detallesProductos) {
        this.detallesProductos = detallesProductos;
    }

    public List<PedidoDetalleReceta> getDetallesRecetas() {
        return detallesRecetas;
    }

    public void setDetallesRecetas(List<PedidoDetalleReceta> detallesRecetas) {
        this.detallesRecetas = detallesRecetas;
    }
}