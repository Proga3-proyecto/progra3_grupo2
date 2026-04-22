package com.licoreria.dao.impl;

import com.licoreria.DBmanager.DBManager;
import com.licoreria.DBmanager.TransactionContext;
import com.licoreria.dao.DetalleRecetaDAO;
import com.licoreria.dominio.pedidos.DetalleElementoReceta;
import com.licoreria.dominio.pedidos.DetalleReceta;
import com.licoreria.dominio.productos.Receta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleRecetaDAOImpl implements DetalleRecetaDAO {

    @Override
    public DetalleReceta get(Long id) throws SQLException {
        DetalleReceta detalle = null;
        String query = "SELECT * FROM Detalle_Receta WHERE id_detalle_receta = ?";

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    detalle = mapResultSetToDetalleReceta(rs);
                }
            }
        }
        return detalle;
    }

    @Override
    public List<DetalleReceta> getAll() throws SQLException {
        List<DetalleReceta> lista = new ArrayList<>();
        String query = "SELECT * FROM Detalle_Receta";

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToDetalleReceta(rs));
            }
        }
        return lista;
    }

    @Override
    public DetalleReceta save(DetalleReceta detalle) throws SQLException {
        return save(detalle, null, null);
    }

    @Override
    public DetalleReceta update(DetalleReceta detalle) throws SQLException {
        return update(detalle, null, null);
    }

    @Override
    public void remove(DetalleReceta detalle) throws SQLException {
        String deleteElementos = "DELETE FROM Detalle_Elemento_Receta WHERE id_detalle_receta = ?";
        String deleteDetalle = "DELETE FROM Detalle_Receta WHERE id_detalle_receta = ?";

        Connection conn = TransactionContext.getConnection();

        try {
            try (PreparedStatement psElementos = conn.prepareStatement(deleteElementos)) {
                psElementos.setLong(1, detalle.getId());
                psElementos.executeUpdate();
            }

            try (PreparedStatement psDetalle = conn.prepareStatement(deleteDetalle)) {
                psDetalle.setLong(1, detalle.getId());
                psDetalle.executeUpdate();
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
    public List<DetalleReceta> getByCarrito(long idCliente) throws SQLException {
        return obtenerDetalles("SELECT * FROM Detalle_Receta WHERE id_cliente_carrito = ?", idCliente);
    }

    @Override
    public List<DetalleReceta> getByPedido(long idPedido) throws SQLException {
        return obtenerDetalles("SELECT * FROM Detalle_Receta WHERE id_pedido = ?", idPedido);
    }

    @Override
    public DetalleReceta save(DetalleReceta detalle, Long idPedido, Long idClienteCarrito) throws SQLException {
        String insertDetalle = "INSERT INTO Detalle_Receta (id_pedido, id_cliente_carrito, id_receta_base, descuento_total, total_impuestos, monto_total) VALUES (?, ?, ?, ?, ?, ?)";
        String insertElementos = "INSERT INTO Detalle_Elemento_Receta (id_detalle_receta, id_elemento_base, cantidad_especifica) VALUES (?, ?, ?)";

        Connection conn = TransactionContext.getConnection();

        try {
            try (PreparedStatement psDetalle = conn.prepareStatement(insertDetalle, Statement.RETURN_GENERATED_KEYS)) {
                setNullableParams(psDetalle, idPedido, idClienteCarrito);
                psDetalle.setLong(3, detalle.getRecetaBase().getId());
                psDetalle.setDouble(4, detalle.getDescuentoTotal());
                psDetalle.setDouble(5, detalle.getTotalImpuestos() != null ? detalle.getTotalImpuestos() : 0.0);
                psDetalle.setDouble(6, detalle.getMontoTotal());

                psDetalle.executeUpdate();

                try (ResultSet rs = psDetalle.getGeneratedKeys()) {
                    if (rs.next()) {
                        long idGenerado = rs.getLong(1);
                        detalle.setId(idGenerado);

                        if (detalle.getElementosDesglosados() != null && !detalle.getElementosDesglosados().isEmpty()) {
                            try (PreparedStatement psElementos = conn.prepareStatement(insertElementos)) {
                                for (DetalleElementoReceta der : detalle.getElementosDesglosados()) {
                                    psElementos.setLong(1, idGenerado);
                                    psElementos.setLong(2, der.getElementoBase().getId());
                                    psElementos.setDouble(3, der.getCantidadEspecifica());
                                    psElementos.addBatch();
                                }
                                psElementos.executeBatch();
                            }
                        }
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
    public DetalleReceta update(DetalleReceta detalle, Long idPedido, Long idClienteCarrito) throws SQLException {
        String updateDetalle = "UPDATE Detalle_Receta SET id_pedido=?, id_cliente_carrito=?, id_receta_base=?, descuento_total=?, total_impuestos=?, monto_total=? WHERE id_detalle_receta=?";
        String deleteElementos = "DELETE FROM Detalle_Elemento_Receta WHERE id_detalle_receta=?";
        String insertElementos = "INSERT INTO Detalle_Elemento_Receta (id_detalle_receta, id_elemento_base, cantidad_especifica) VALUES (?, ?, ?)";

        Connection conn = TransactionContext.getConnection();

        try {
            try (PreparedStatement psDetalle = conn.prepareStatement(updateDetalle)) {
                setNullableParams(psDetalle, idPedido, idClienteCarrito);
                psDetalle.setLong(3, detalle.getRecetaBase().getId());
                psDetalle.setDouble(4, detalle.getDescuentoTotal());
                psDetalle.setDouble(5, detalle.getTotalImpuestos() != null ? detalle.getTotalImpuestos() : 0.0);
                psDetalle.setDouble(6, detalle.getMontoTotal());
                psDetalle.setLong(7, detalle.getId());
                psDetalle.executeUpdate();
            }

            try (PreparedStatement psDelete = conn.prepareStatement(deleteElementos)) {
                psDelete.setLong(1, detalle.getId());
                psDelete.executeUpdate();
            }

            if (detalle.getElementosDesglosados() != null && !detalle.getElementosDesglosados().isEmpty()) {
                try (PreparedStatement psElementos = conn.prepareStatement(insertElementos)) {
                    for (DetalleElementoReceta der : detalle.getElementosDesglosados()) {
                        psElementos.setLong(1, detalle.getId());
                        psElementos.setLong(2, der.getElementoBase().getId());
                        psElementos.setDouble(3, der.getCantidadEspecifica());
                        psElementos.addBatch();
                    }
                    psElementos.executeBatch();
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

    private List<DetalleReceta> obtenerDetalles(String query, long idFiltro) throws SQLException {
        List<DetalleReceta> lista = new ArrayList<>();
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setLong(1, idFiltro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToDetalleReceta(rs));
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

    private DetalleReceta mapResultSetToDetalleReceta(ResultSet rs) throws SQLException {
        Receta r = new Receta(null, null, null, null);
        r.setId(rs.getLong("id_receta_base"));

        DetalleReceta dr = new DetalleReceta(r);
        dr.setId(rs.getLong("id_detalle_receta"));
        dr.setDescuentoTotal(rs.getDouble("descuento_total"));
        dr.setTotalImpuestos(rs.getDouble("total_impuestos"));
        dr.setMontoTotal(rs.getDouble("monto_total"));

        return dr;
    }
}