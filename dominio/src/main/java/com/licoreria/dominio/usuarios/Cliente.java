package com.licoreria.dominio.usuarios;

import com.licoreria.dominio.carrito.DetalleProducto;
import com.licoreria.dominio.carrito.DetalleReceta;
import com.licoreria.dominio.carrito.Pedido;

import java.util.Date;
import java.util.List;

public class Cliente extends Usuario {
    private String telefono;
    private Date fechaNacimiento;
    private Pedido pedidoActivo; // Relación 0..1 hacia el pedido en curso
    private List<ClienteDireccion> direcciones;
    private List<DetalleProducto> carritoProductos;
    private List<DetalleReceta> carritoRecetas;

    public Cliente(){

    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Pedido getPedidoActivo() {
        return pedidoActivo;
    }

    public void setPedidoActivo(Pedido pedidoActivo) {
        this.pedidoActivo = pedidoActivo;
    }

    public List<ClienteDireccion> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<ClienteDireccion> direcciones) {
        this.direcciones = direcciones;
    }

    public List<DetalleProducto> getCarritoProductos() {
        return carritoProductos;
    }

    public void setCarritoProductos(List<DetalleProducto> carritoProductos) {
        this.carritoProductos = carritoProductos;
    }

    public List<DetalleReceta> getCarritoRecetas() {
        return carritoRecetas;
    }

    public void setCarritoRecetas(List<DetalleReceta> carritoRecetas) {
        this.carritoRecetas = carritoRecetas;
    }

}