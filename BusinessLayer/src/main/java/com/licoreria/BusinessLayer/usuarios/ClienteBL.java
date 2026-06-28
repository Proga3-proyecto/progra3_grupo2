package com.licoreria.BusinessLayer.usuarios;

import com.licoreria.dominio.carrito.DetalleProducto;
import com.licoreria.dominio.carrito.DetalleReceta;
import com.licoreria.dominio.catalogo.Producto;
import com.licoreria.dominio.catalogo.Receta;
import com.licoreria.dominio.usuarios.Cliente;

import java.util.List;

public interface ClienteBL {
    List<Cliente> getAll();

    Cliente get(int id);

    Cliente save(Cliente cliente);

    Cliente update(Cliente cliente);

    void delete(int id);

    List<DetalleProducto> getProductosEnCarrito(int idCliente);
    List<DetalleReceta> getRecetasEnCarrito(int idCliente);


    Cliente validarCredenciales(String usuario, String password);
    List<com.licoreria.dominio.carrito.Pedido> getPedidos(int idCliente);
    void agregarProductoAlCarrito(int idCliente, int idProducto, int cantidad);
    void agregarRecetaAlCarrito(int idCliente, int idReceta, int cantidad);
    void eliminarProductoDelCarrito(int idCliente, int idProducto);
    void eliminarRecetaDelCarrito(int idCliente, int idReceta);
    void actualizarCantidadProductoEnCarrito(int idCliente, int idProducto, int cantidad);
    void actualizarCantidadRecetaEnCarrito(int idCliente, int idReceta, int cantidad);

}