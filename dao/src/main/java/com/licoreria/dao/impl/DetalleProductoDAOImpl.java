package com.licoreria.dao.impl;

import com.licoreria.DBmanager.DBManager;
import com.licoreria.DBmanager.TransactionContext;
import com.licoreria.dao.DetalleProductoDAO;
import com.licoreria.dominio.pedidos.DetalleProducto;
import com.licoreria.dominio.productos.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleProductoDAOImpl implements DetalleProductoDAO {

    @Override
    public DetalleProducto get(Long id) throws SQLException {
        DetalleProducto detalle = null;
        String query = "SELECT * FROM Detalle_Producto WHERE id_detalle_producto = ?";

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    detalle = mapResultSetToDetalleProducto(rs);
                }
            }
        }
        return detalle;
    }

    @Override
    public List<DetalleProducto> getAll() throws SQLException {
        List<DetalleProducto> lista = new ArrayList<>();
        String query = "SELECT * FROM Detalle_Producto";

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToDetalleProducto(rs));
            }
        }
        return lista;
    }

    @Override
    public DetalleProducto save(DetalleProducto detalle) throws SQLException {
        return save(detalle, null, null);
    }

    @Override
    public DetalleProducto update(DetalleProducto detalle) throws SQLException {
        return update(detalle, null, null);
    }

    @Override
    public void remove(DetalleProducto detalle) throws SQLException {
        String query = "DELETE FROM Detalle_Producto WHERE id_detalle_producto = ?";

        Connection conn = TransactionContext.getConnection();

        try {
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setLong(1, detalle.getId());
                ps.executeUpdate();
            }
            TransactionContext.commit();
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public List<DetalleProducto> getByCarrito(long idCliente) throws SQLException {
        return obtenerDetalles("SELECT * FROM Detalle_Producto WHERE id_cliente_carrito = ?", idCliente);
    }

    @Override
    public List<DetalleProducto> getByPedido(long idPedido) throws SQLException {
        return obtenerDetalles("SELECT * FROM Detalle_Producto WHERE id_pedido = ?", idPedido);
    }

    @Override
    public DetalleProducto save(DetalleProducto detalle, Long idPedido, Long idClienteCarrito) throws SQLException {
        String query = "INSERT INTO Detalle_Producto (id_pedido, id_cliente_carrito, id_producto, cantidad, descuento_total, total_impuestos, monto_total) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = TransactionContext.getConnection();

        try {
            try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                setNullableParams(ps, idPedido, idClienteCarrito);
                ps.setLong(3, detalle.getProducto().getId());
                ps.setInt(4, detalle.getCantidad());
                ps.setDouble(5, detalle.getDescuentoTotal());
                ps.setDouble(6, detalle.getTotalImpuestos() != null ? detalle.getTotalImpuestos() : 0.0);
                ps.setDouble(7, detalle.getMontoTotal());

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        detalle.setId(rs.getLong(1));
                    }
                }
            }
            TransactionContext.commit();
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
        return detalle;
    }

    @Override
    public DetalleProducto update(DetalleProducto detalle, Long idPedido, Long idClienteCarrito) throws SQLException {
        String query = "UPDATE Detalle_Producto SET id_pedido=?, id_cliente_carrito=?, id_producto=?, cantidad=?, descuento_total=?, total_impuestos=?, monto_total=? WHERE id_detalle_producto=?";

        Connection conn = TransactionContext.getConnection();

        try {
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                setNullableParams(ps, idPedido, idClienteCarrito);
                ps.setLong(3, detalle.getProducto().getId());
                ps.setInt(4, detalle.getCantidad());
                ps.setDouble(5, detalle.getDescuentoTotal());
                ps.setDouble(6, detalle.getTotalImpuestos() != null ? detalle.getTotalImpuestos() : 0.0);
                ps.setDouble(7, detalle.getMontoTotal());
                ps.setLong(8, detalle.getId());

                ps.executeUpdate();
            }
            TransactionContext.commit();
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
        return detalle;
    }

    private List<DetalleProducto> obtenerDetalles(String query, long idFiltro) throws SQLException {
        List<DetalleProducto> lista = new ArrayList<>();
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, idFiltro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToDetalleProducto(rs));
                }
            }
        }
        return lista;
    }

    private void setNullableParams(PreparedStatement ps, Long idPedido, Long idClienteCarrito) throws SQLException {
        if (idPedido != null) ps.setLong(1, idPedido);
        else ps.setNull(1, java.sql.Types.INTEGER);

        if (idClienteCarrito != null) ps.setLong(2, idClienteCarrito);
        else ps.setNull(2, java.sql.Types.INTEGER);
    }

    private DetalleProducto mapResultSetToDetalleProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getLong("id_producto"));

        DetalleProducto dp = new DetalleProducto(p, rs.getInt("cantidad"));
        dp.setId(rs.getLong("id_detalle_producto"));
        dp.setDescuentoTotal(rs.getDouble("descuento_total"));
        dp.setTotalImpuestos(rs.getDouble("total_impuestos"));
        dp.setMontoTotal(rs.getDouble("monto_total"));

        return dp;
    }
}