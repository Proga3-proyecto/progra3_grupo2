package com.licoreria.BusinessLayer.DetalleProductoService;

import com.licoreria.dominio.pedidos.DetalleProducto;

import java.util.List;

public interface IDetalleProductoService {
    DetalleProducto obtenerPorId(Long id);
    List<DetalleProducto> listarTodos();
    DetalleProducto crearEnPedido(DetalleProducto detalle, Long idPedido);
    DetalleProducto crearEnCarrito(DetalleProducto detalle, Long idCliente);
    DetalleProducto actualizar(DetalleProducto detalle);
    void eliminar(DetalleProducto detalle);
    List<DetalleProducto> obtenerPorPedido(Long idPedido);
    List<DetalleProducto> obtenerPorCarrito(Long idCliente);
    void recalcularMonto(DetalleProducto detalle);
}