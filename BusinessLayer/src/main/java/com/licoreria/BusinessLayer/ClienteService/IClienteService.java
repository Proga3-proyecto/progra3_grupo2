package com.licoreria.BusinessLayer.ClienteService;

import com.licoreria.dominio.pedidos.DetalleProducto;
import com.licoreria.dominio.pedidos.DetalleReceta;
import com.licoreria.dominio.usuarios.Cliente;

import java.util.List;

public interface IClienteService  {
    Cliente obtenerPorId(Long id);
    List<Cliente> listarTodos();
    Cliente crear(Cliente cliente);
    Cliente actualizar(Cliente cliente);
    void eliminar(Cliente cliente);

    List<DetalleProducto> obtenerCarritoProductos(Cliente cliente);
    void agregarProductoAlCarrito(Cliente cliente, DetalleProducto detalle);
    void eliminarProductoDelCarrito(Cliente cliente, Long idDetalleProducto);
    void limpiarCarritoProductos(Cliente cliente);

    List<DetalleReceta> obtenerCarritoRecetas(Cliente cliente);
    void agregarRecetaAlCarrito(Cliente cliente, DetalleReceta detalle);
    void eliminarRecetaDelCarrito(Cliente cliente, Long idDetalleReceta);
    void limpiarCarritoRecetas(Cliente cliente);
}