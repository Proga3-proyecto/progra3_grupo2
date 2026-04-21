package com.licoreria.app.dominio.pedidos;


import com.licoreria.app.dominio.usuarios.Cliente;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class Pedido {
    private long idPedido;
    private Cliente cliente;
    private Date fechaPedido;
    private List<DetallePedido> detalles;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private double precioTotal;
    private double precioDelivery;
    private EstadoPedido estado;
    private String direccionDestino;

    public  Pedido(){

    }
    public Pedido(
            Cliente cliente,
            Date fechaPedido,
            List<DetallePedido> detalles,
            LocalTime horaInicio,
            String direccionDestino) {
        this.cliente = cliente;
        this.fechaPedido = fechaPedido;
        this.detalles = detalles;
        this.horaInicio = horaInicio;
        this.direccionDestino = direccionDestino;
    }

    public long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(long idPedido) {
        this.idPedido = idPedido;
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

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public double getPrecioDelivery() {
        return precioDelivery;
    }

    public void setPrecioDelivery(double precioDelivery) {
        this.precioDelivery = precioDelivery;
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


}
