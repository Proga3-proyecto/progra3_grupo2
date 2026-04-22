package com.licoreria.dao;

import com.licoreria.dominio.pedidos.DetalleReceta;

import java.sql.SQLException;
import java.util.List;

public interface DetalleRecetaDAO extends BaseDAO<DetalleReceta, Long> {
    List<DetalleReceta> getByCarrito(long idCliente) throws SQLException;
    List<DetalleReceta> getByPedido(long idPedido) throws SQLException;
    DetalleReceta save(DetalleReceta detalle, Long idPedido, Long idClienteCarrito) throws SQLException;
    DetalleReceta update(DetalleReceta detalle, Long idPedido, Long idClienteCarrito) throws SQLException;
}