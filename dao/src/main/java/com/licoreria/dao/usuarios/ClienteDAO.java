package com.licoreria.dao.usuarios;

import com.licoreria.dao.BaseDAO;
import com.licoreria.dominio.usuarios.Cliente;

import java.sql.Connection;
import java.sql.SQLException;

public interface ClienteDAO extends BaseDAO<Cliente, Integer> {
    Cliente getPorCorreo(Connection con, String correo, String contrasena) throws SQLException;
    void agregarProductoAlCarrito(Connection con, int idCliente, int idProducto, int cantidad, double descuentoTotal, double montoTotal) throws SQLException;
    void agregarRecetaAlCarrito(Connection con, int idCliente, int idReceta, int cantidad, double descuentoTotal, double montoTotal) throws SQLException;
    void eliminarProductoDelCarrito(Connection con, int idCliente, int idProducto) throws SQLException;
    void eliminarRecetaDelCarrito(Connection con, int idCliente, int idReceta) throws SQLException;
    void actualizarCantidadProductoEnCarrito(Connection con, int idCliente, int idProducto, int cantidad, double descuentoTotal, double montoTotal) throws SQLException;
    void actualizarCantidadRecetaEnCarrito(Connection con, int idCliente, int idReceta, int cantidad, double descuentoTotal, double montoTotal) throws SQLException;
}