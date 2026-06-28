package com.licoreria.dao.carrito;

import com.licoreria.dao.BaseDAO;
import com.licoreria.dominio.carrito.Pedido;

public interface PedidoDAO extends BaseDAO<Pedido, Integer> {
    java.util.List<Pedido> getPedidosPorCliente(java.sql.Connection con, Integer idCliente) throws java.sql.SQLException;
}