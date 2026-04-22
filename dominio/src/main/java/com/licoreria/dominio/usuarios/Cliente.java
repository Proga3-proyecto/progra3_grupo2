package com.licoreria.dominio.usuarios;

import com.licoreria.dominio.pedidos.DetalleProducto;
import com.licoreria.dominio.pedidos.DetalleReceta;
import com.licoreria.dominio.pedidos.Pedido;
import com.licoreria.dominio.productos.Receta;

import java.util.Date;
import java.util.List;

public class Cliente extends Usuario {
    private List<DetalleProducto> carritoProductos;
    private List<DetalleReceta> carritoRecetas;

    private Pedido pedidoActivo;
    private List<String> direcciones;

    private List<Receta> recetasFavoritos;

    public Cliente(){


    }

    public Cliente(
            String dni,
            String nombre,
            String correo,
            String telefono,
            String apellidoCompleto,
            Date fechaNacimiento,
            String contraseniaHash) {
        super(dni, nombre, correo, telefono, apellidoCompleto, fechaNacimiento, contraseniaHash);
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

    public Pedido getPedidoActivo() {
        return pedidoActivo;
    }

    public void setPedidoActivo(Pedido pedidoActivo) {
        this.pedidoActivo = pedidoActivo;
    }

    public List<String> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<String> direcciones) {
        this.direcciones = direcciones;
    }



    public List<Receta> getRecetasFavoritos() {
        return recetasFavoritos;
    }

    public void setRecetasFavoritos(List<Receta> recetasFavoritos) {
        this.recetasFavoritos = recetasFavoritos;
    }


}
