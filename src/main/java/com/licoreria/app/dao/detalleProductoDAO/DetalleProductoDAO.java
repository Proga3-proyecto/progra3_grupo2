package com.licoreria.app.dao.detalleProductoDAO;

import com.licoreria.app.modelo.pedidos.DetalleProducto;

import java.util.List;

public interface DetalleProductoDAO {
    List<DetalleProducto> getByCarrito(long idCliente);
    List<DetalleProducto> getByPedido(long idPedido);

    void save(DetalleProducto detalle, Long idPedido, Long idClienteCarrito);

    void update(DetalleProducto detalle, Long idPedido, Long idClienteCarrito);
    void delete(long idDetalleProducto);
}