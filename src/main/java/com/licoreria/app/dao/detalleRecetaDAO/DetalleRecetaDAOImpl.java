package com.licoreria.app.dao.detalleRecetaDAO;

import com.licoreria.app.dao.conexionBD.ConexionDB;
import com.licoreria.app.modelo.pedidos.DetalleElementoReceta;
import com.licoreria.app.modelo.pedidos.DetalleReceta;
import com.licoreria.app.modelo.productos.Receta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleRecetaDAOImpl implements DetalleRecetaDAO {

    @Override
    public List<DetalleReceta> getByCarrito(long idCliente) {
        return obtenerDetalles("SELECT * FROM Detalle_Receta WHERE id_cliente_carrito = ?", idCliente);
    }

    @Override
    public List<DetalleReceta> getByPedido(long idPedido) {
        return obtenerDetalles("SELECT * FROM Detalle_Receta WHERE id_pedido = ?", idPedido);
    }

    @Override
    public void save(DetalleReceta detalle, Long idPedido, Long idClienteCarrito) {
        String insertDetalle = "INSERT INTO Detalle_Receta (id_pedido, id_cliente_carrito, id_receta_base, descuento_total, monto_total) VALUES (?, ?, ?, ?, ?)";
        String insertElementos = "INSERT INTO Detalle_Elemento_Receta (id_detalle_receta, id_elemento_base, cantidad_especifica) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = ConexionDB.getConexion();
            conn.setAutoCommit(false);

            try (PreparedStatement psDetalle = conn.prepareStatement(insertDetalle, Statement.RETURN_GENERATED_KEYS)) {
                setNullableParams(psDetalle, idPedido, idClienteCarrito);
                psDetalle.setLong(3, detalle.getRecetaBase().getId());
                psDetalle.setDouble(4, detalle.getDescuentoTotal());
                psDetalle.setDouble(5, detalle.getMontoTotal());

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
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            e.printStackTrace();
        } finally {
            if (conn != null) { try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
    }

    @Override
    public void update(DetalleReceta detalle, Long idPedido, Long idClienteCarrito) {
        String updateDetalle = "UPDATE Detalle_Receta SET id_pedido=?, id_cliente_carrito=?, id_receta_base=?, descuento_total=?, monto_total=? WHERE id_detalle_receta=?";
        String deleteElementos = "DELETE FROM Detalle_Elemento_Receta WHERE id_detalle_receta=?";
        String insertElementos = "INSERT INTO Detalle_Elemento_Receta (id_detalle_receta, id_elemento_base, cantidad_especifica) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = ConexionDB.getConexion();
            conn.setAutoCommit(false);

            try (PreparedStatement psDetalle = conn.prepareStatement(updateDetalle)) {
                setNullableParams(psDetalle, idPedido, idClienteCarrito);
                psDetalle.setLong(3, detalle.getRecetaBase().getId());
                psDetalle.setDouble(4, detalle.getDescuentoTotal());
                psDetalle.setDouble(5, detalle.getMontoTotal());
                psDetalle.setLong(6, detalle.getId());
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
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            e.printStackTrace();
        } finally {
            if (conn != null) { try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
    }

    @Override
    public void delete(long idDetalleReceta) {
        String query = "DELETE FROM Detalle_Receta WHERE id_detalle_receta = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, idDetalleReceta);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private List<DetalleReceta> obtenerDetalles(String query, long idFiltro) {
        List<DetalleReceta> lista = new ArrayList<>();
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setLong(1, idFiltro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Receta r = new Receta(null, null, null, null);
                    r.setId(rs.getLong("id_receta_base"));

                    DetalleReceta dr = new DetalleReceta(r);
                    dr.setId(rs.getLong("id_detalle_receta"));
                    dr.setDescuentoTotal(rs.getDouble("descuento_total"));
                    dr.setMontoTotal(rs.getDouble("monto_total"));
                    lista.add(dr);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private void setNullableParams(PreparedStatement ps, Long idPedido, Long idClienteCarrito) throws SQLException {
        if (idPedido != null) ps.setLong(1, idPedido);
        else ps.setNull(1, java.sql.Types.INTEGER);

        if (idClienteCarrito != null) ps.setLong(2, idClienteCarrito);
        else ps.setNull(2, java.sql.Types.INTEGER);
    }
}