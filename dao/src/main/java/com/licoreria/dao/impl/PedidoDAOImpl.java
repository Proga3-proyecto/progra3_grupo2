package com.licoreria.dao.impl;

import com.licoreria.DBmanager.DBManager;
import com.licoreria.DBmanager.TransactionContext;
import com.licoreria.dao.PedidoDAO;
import com.licoreria.dominio.pedidos.EstadoPedido;
import com.licoreria.dominio.pedidos.Pedido;
import com.licoreria.dominio.usuarios.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {

    @Override
    public Pedido get(Long id) throws SQLException {
        Pedido pedido = null;
        String query = "SELECT * FROM Pedido WHERE id_pedido = ?";

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pedido = mapResultSetToPedido(rs);
                }
            }
        }
        return pedido;
    }

    @Override
    public List<Pedido> getAll() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String query = "SELECT * FROM Pedido";

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                pedidos.add(mapResultSetToPedido(rs));
            }
        }
        return pedidos;
    }

    @Override
    public Pedido save(Pedido pedido) throws SQLException {
        String query = "INSERT INTO Pedido (id_cliente, fecha_pedido, hora_inicio, hora_fin, precio_total, total_impuestos, precio_delivery, estado, direccion_destino) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = TransactionContext.getConnection();

        try {
            try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, pedido.getCliente().getId());
                ps.setDate(2, pedido.getFechaPedido() != null ? new java.sql.Date(pedido.getFechaPedido().getTime()) : new java.sql.Date(System.currentTimeMillis()));
                ps.setTime(3, pedido.getHoraInicio() != null ? java.sql.Time.valueOf(pedido.getHoraInicio()) : null);
                ps.setTime(4, pedido.getHoraFin() != null ? java.sql.Time.valueOf(pedido.getHoraFin()) : null);
                ps.setDouble(5, pedido.getPrecioTotal());
                ps.setDouble(6, pedido.getTotalImpuestos() != null ? pedido.getTotalImpuestos() : 0.0);
                ps.setDouble(7, pedido.getPrecioDelivery());
                ps.setString(8, pedido.getEstado() != null ? pedido.getEstado().name() : EstadoPedido.PENDIENTE.name());
                ps.setString(9, pedido.getDireccionDestino());

                int affectedRows = ps.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            pedido.setIdPedido(rs.getLong(1));
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
        return pedido;
    }

    @Override
    public Pedido update(Pedido pedido) throws SQLException {
        String query = "UPDATE Pedido SET id_cliente=?, fecha_pedido=?, hora_inicio=?, hora_fin=?, precio_total=?, total_impuestos=?, precio_delivery=?, estado=?, direccion_destino=? WHERE id_pedido=?";

        Connection conn = TransactionContext.getConnection();

        try {
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setLong(1, pedido.getCliente().getId());
                ps.setDate(2, pedido.getFechaPedido() != null ? new java.sql.Date(pedido.getFechaPedido().getTime()) : null);
                ps.setTime(3, pedido.getHoraInicio() != null ? java.sql.Time.valueOf(pedido.getHoraInicio()) : null);
                ps.setTime(4, pedido.getHoraFin() != null ? java.sql.Time.valueOf(pedido.getHoraFin()) : null);
                ps.setDouble(5, pedido.getPrecioTotal());
                ps.setDouble(6, pedido.getTotalImpuestos() != null ? pedido.getTotalImpuestos() : 0.0);
                ps.setDouble(7, pedido.getPrecioDelivery());
                ps.setString(8, pedido.getEstado() != null ? pedido.getEstado().name() : EstadoPedido.PENDIENTE.name());
                ps.setString(9, pedido.getDireccionDestino());
                ps.setLong(10, pedido.getIdPedido());

                ps.executeUpdate();
            }
            TransactionContext.commit();
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
        return pedido;
    }

    @Override
    public void remove(Pedido pedido) throws SQLException {
        String query = "DELETE FROM Pedido WHERE id_pedido = ?";
        Connection conn = TransactionContext.getConnection();

        try {
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setLong(1, pedido.getIdPedido());
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

    private Pedido mapResultSetToPedido(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setIdPedido(rs.getLong("id_pedido"));

        Cliente c = new Cliente();
        c.setId(rs.getLong("id_cliente"));
        p.setCliente(c);

        Date fechaSql = rs.getDate("fecha_pedido");
        if (fechaSql != null) {
            p.setFechaPedido(new java.util.Date(fechaSql.getTime()));
        }

        Time horaInicioSql = rs.getTime("hora_inicio");
        if (horaInicioSql != null) {
            p.setHoraInicio(horaInicioSql.toLocalTime());
        }

        Time horaFinSql = rs.getTime("hora_fin");
        if (horaFinSql != null) {
            p.setHoraFin(horaFinSql.toLocalTime());
        }

        p.setPrecioTotal(rs.getDouble("precio_total"));
        p.setTotalImpuestos(rs.getDouble("total_impuestos"));
        p.setPrecioDelivery(rs.getDouble("precio_delivery"));
        p.setDireccionDestino(rs.getString("direccion_destino"));

        String estadoBD = rs.getString("estado");
        if (estadoBD != null) {
            try {
                p.setEstado(EstadoPedido.valueOf(estadoBD));
            } catch (IllegalArgumentException e) {
                p.setEstado(EstadoPedido.PENDIENTE);
            }
        }
        return p;
    }
}