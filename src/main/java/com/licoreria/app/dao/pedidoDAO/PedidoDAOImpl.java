package com.licoreria.app.dao.pedidoDAO;

import com.licoreria.app.dao.conexionBD.ConexionDB;
import com.licoreria.app.modelo.pedidos.EstadoPedido;
import com.licoreria.app.modelo.pedidos.Pedido;
import com.licoreria.app.modelo.usuarios.Cliente;
import com.licoreria.app.modelo.usuarios.Motorizado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {

    @Override
    public Pedido get(long id) {
        Pedido pedido = null;
        String query = "SELECT * FROM Pedido WHERE id_pedido = ?";

        try (Connection conn = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pedido = mapResultSetToPedido(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedido;
    }

    @Override
    public List<Pedido> getAll() {
        List<Pedido> pedidos = new ArrayList<>();
        String query = "SELECT * FROM Pedido";

        try (Connection conn = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                pedidos.add(mapResultSetToPedido(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }

    @Override
    public long save(Pedido pedido) {
        String query = "INSERT INTO Pedido (id_cliente, id_motorizado, fecha_pedido, hora_inicio, hora_fin, precio_total, precio_delivery, estado, direccion_destino) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        long idGenerado = -1;

        try (Connection conn = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, pedido.getCliente().getId());

            // El motorizado puede ser null al crear el pedido (se asigna después)
            if (pedido.getMotorizado() != null && pedido.getMotorizado().getId() > 0) {
                ps.setLong(2, pedido.getMotorizado().getId());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }

            // Manejo de fechas y horas
            ps.setDate(3, pedido.getFechaPedido() != null ? new java.sql.Date(pedido.getFechaPedido().getTime()) : new java.sql.Date(System.currentTimeMillis()));
            ps.setTime(4, pedido.getHoraInicio() != null ? java.sql.Time.valueOf(pedido.getHoraInicio()) : null);
            ps.setTime(5, pedido.getHoraFin() != null ? java.sql.Time.valueOf(pedido.getHoraFin()) : null);

            ps.setDouble(6, pedido.getPrecioTotal());
            ps.setDouble(7, pedido.getPrecioDelivery());

            // Estado por defecto
            String estadoStr = pedido.getEstado() != null ? pedido.getEstado().name() : EstadoPedido.PENDIENTE.name();
            ps.setString(8, estadoStr);

            ps.setString(9, pedido.getDireccionDestino());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGenerado = rs.getLong(1);
                        pedido.setIdPedido(idGenerado);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idGenerado;
    }

    @Override
    public void update(Pedido pedido) {
        String query = "UPDATE Pedido SET id_cliente=?, id_motorizado=?, fecha_pedido=?, hora_inicio=?, hora_fin=?, precio_total=?, precio_delivery=?, estado=?, direccion_destino=? WHERE id_pedido=?";

        try (Connection conn = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setLong(1, pedido.getCliente().getId());

            if (pedido.getMotorizado() != null && pedido.getMotorizado().getId() > 0) {
                ps.setLong(2, pedido.getMotorizado().getId());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }

            ps.setDate(3, pedido.getFechaPedido() != null ? new java.sql.Date(pedido.getFechaPedido().getTime()) : null);
            ps.setTime(4, pedido.getHoraInicio() != null ? java.sql.Time.valueOf(pedido.getHoraInicio()) : null);
            ps.setTime(5, pedido.getHoraFin() != null ? java.sql.Time.valueOf(pedido.getHoraFin()) : null);
            ps.setDouble(6, pedido.getPrecioTotal());
            ps.setDouble(7, pedido.getPrecioDelivery());
            ps.setString(8, pedido.getEstado() != null ? pedido.getEstado().name() : EstadoPedido.PENDIENTE.name());
            ps.setString(9, pedido.getDireccionDestino());
            ps.setLong(10, pedido.getIdPedido());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long idPedido) {
        String query = "DELETE FROM Pedido WHERE id_pedido = ?";
        try (Connection conn = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, idPedido);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- MÉTODOS AUXILIARES ---

    private Pedido mapResultSetToPedido(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setIdPedido(rs.getLong("id_pedido"));

        // Reconstruimos el Cliente (Solo con ID. En la capa de servicio puedes usar ClienteDAO para traerlo completo si lo necesitas)
        Cliente c = new Cliente();
        c.setId(rs.getLong("id_cliente"));
        p.setCliente(c);

        // Reconstruimos el Motorizado si existe
        long idMotorizado = rs.getLong("id_motorizado");
        if (!rs.wasNull()) {
            // El constructor de motorizado requiere muchos parámetros, usamos setters para instanciarlo rápido
            Motorizado m = new Motorizado(null, null, null, null, null, null, null, null, 0, 0);
            m.setId(idMotorizado);
            p.setMotorizado(m);
        }

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
        p.setPrecioDelivery(rs.getDouble("precio_delivery"));
        p.setDireccionDestino(rs.getString("direccion_destino"));

        String estadoBD = rs.getString("estado");
        if (estadoBD != null) {
            try {
                p.setEstado(EstadoPedido.valueOf(estadoBD));
            } catch (IllegalArgumentException e) {
                p.setEstado(EstadoPedido.PENDIENTE); // Fallback por defecto si hay un error en BD
            }
        }

        // Nota: Los detalles (List<DetallePedido>) NO se cargan aquí para evitar consultas circulares masivas.
        // Se deben cargar usando el DetalleProductoDAO y DetalleRecetaDAO desde tu controlador o servicio.

        return p;
    }
}