package com.licoreria.dao.usuarios;

import com.licoreria.dao.BaseDAO;
import com.licoreria.dominio.carrito.DetalleProducto;
import com.licoreria.dominio.carrito.DetalleReceta;
import com.licoreria.dominio.usuarios.Cliente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ClienteDAO extends BaseDAO<Cliente, Integer> {
    Cliente getPorCorreo(Connection con, String correo, String contrasena) throws SQLException;

    List<DetalleProducto> getDetalleProductos(Connection con, int idCliente) throws SQLException;

    List<DetalleReceta> getDetalleReceta(Connection con, int idCliente) throws SQLException;

    void agregarProductoAlCarrito(Connection con, int idCliente, int idProducto, int cantidad, double descuentoTotal, double montoTotal) throws SQLException;

    void agregarRecetaAlCarrito(Connection con, int idCliente, int idReceta, int cantidad, double descuentoTotal, double montoTotal) throws SQLException;

    void eliminarProductoDelCarrito(Connection con, int idCliente, int idProducto) throws SQLException;

    void eliminarRecetaDelCarrito(Connection con, int idCliente, int idReceta) throws SQLException;

    void actualizarCantidadProductoEnCarrito(Connection con, int idCliente, int idProducto, int cantidad, double descuentoTotal, double montoTotal) throws SQLException;

    void actualizarCantidadRecetaEnCarrito(Connection con, int idCliente, int idReceta, int cantidad, double descuentoTotal, double montoTotal) throws SQLException;

    Map<Integer, Cliente> getByIds(Connection con, List<Integer> ids) throws SQLException;

    void limpiarPedido(Connection con, int idCliente) throws SQLException;
}