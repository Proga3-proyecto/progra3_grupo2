package com.licoreria.dao;

import com.licoreria.dominio.pedidos.DetalleProducto;

import java.sql.SQLException;
import java.util.List;

public interface DetalleProductoDAO extends BaseDAO<DetalleProducto, Long> {
    List<DetalleProducto> getByCarrito(long idCliente) throws SQLException;
    List<DetalleProducto> getByPedido(long idPedido) throws SQLException;
    DetalleProducto save(DetalleProducto detalle, Long idPedido, Long idClienteCarrito) throws SQLException;
    DetalleProducto update(DetalleProducto detalle, Long idPedido, Long idClienteCarrito) throws SQLException;

}
