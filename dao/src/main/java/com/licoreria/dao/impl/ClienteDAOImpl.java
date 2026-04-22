package com.licoreria.dao.impl;

import com.licoreria.DBmanager.DBManager;
import com.licoreria.DBmanager.TransactionContext;
import com.licoreria.dao.ClienteDAO;
import com.licoreria.dao.utils.DateUtils;
import com.licoreria.dominio.pedidos.Pedido;
import com.licoreria.dominio.usuarios.Cliente;
import com.licoreria.dominio.usuarios.EstadoCuenta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public Cliente get(Long id) throws SQLException {
        Cliente cliente = null;
        String query = "SELECT u.*, c.id_pedido_activo FROM Usuario u INNER JOIN Cliente c ON u.id_usuario = c.id_usuario WHERE u.id_usuario = ?";

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cliente = mapResultSetToCliente(rs);
                }
            }
        }
        return cliente;
    }

    @Override
    public List<Cliente> getAll() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String query = "SELECT u.*, c.id_pedido_activo FROM Usuario u INNER JOIN Cliente c ON u.id_usuario = c.id_usuario";

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clientes.add(mapResultSetToCliente(rs));
            }
        }
        return clientes;
    }

    @Override
    public Cliente save(Cliente cliente) throws SQLException {
        String insertUsuario = "INSERT INTO Usuario (dni, nombre, apellido_completo, fecha_nacimiento, fecha_creacion_cuenta, correo, contrasena_hash, telefono, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertCliente = "INSERT INTO Cliente (id_usuario, id_pedido_activo) VALUES (?, ?)";

        Connection conn = TransactionContext.getConnection();

        try {
            try (PreparedStatement psUsuario = conn.prepareStatement(insertUsuario, Statement.RETURN_GENERATED_KEYS)) {
                psUsuario.setString(1, cliente.getDni());
                psUsuario.setString(2, cliente.getNombre());
                psUsuario.setString(3, cliente.getApellidoCompleto());
                psUsuario.setDate(4, DateUtils.toSqlDate(cliente.getFechaNacimiento()));
                psUsuario.setDate(5, DateUtils.toSqlDate(cliente.getFechaCreacionCuenta(), System.currentTimeMillis()));
                psUsuario.setString(6, cliente.getCorreo());
                psUsuario.setString(7, cliente.getContraseniaHash());
                psUsuario.setString(8, cliente.getTelefono());
                psUsuario.setString(9, cliente.getEstado() != null ? cliente.getEstado().name(): "ACTIVA");

                int affectedRows = psUsuario.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Fallo al crear el usuario, no se afectaron filas.");
                }

                try (ResultSet generatedKeys = psUsuario.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long idGenerado = generatedKeys.getLong(1);
                        cliente.setId(idGenerado);

                        try (PreparedStatement psCliente = conn.prepareStatement(insertCliente)) {
                            psCliente.setLong(1, idGenerado);

                            if (cliente.getPedidoActivo() != null) {
                                psCliente.setLong(2, cliente.getPedidoActivo().getIdPedido());
                            } else {
                                psCliente.setNull(2, java.sql.Types.INTEGER);
                            }
                            psCliente.executeUpdate();
                        }
                    } else {
                        throw new SQLException("Fallo al crear el usuario, no se obtuvo el ID.");
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
        return cliente;
    }

    @Override
    public Cliente update(Cliente cliente) throws SQLException {
        String updateUsuario = "UPDATE Usuario SET dni=?, nombre=?, apellido_completo=?, fecha_nacimiento=?, correo=?, telefono=?, estado=?, contrasena_hash=? WHERE id_usuario=?";
        String updateCliente = "UPDATE Cliente SET id_pedido_activo=? WHERE id_usuario=?";

        Connection conn = TransactionContext.getConnection();

        try {
            try (PreparedStatement psUsuario = conn.prepareStatement(updateUsuario)) {
                psUsuario.setString(1, cliente.getDni());
                psUsuario.setString(2, cliente.getNombre());
                psUsuario.setString(3, cliente.getApellidoCompleto());
                psUsuario.setDate(4, DateUtils.toSqlDate(cliente.getFechaNacimiento()));
                psUsuario.setString(5, cliente.getCorreo());
                psUsuario.setString(6, cliente.getTelefono());
                psUsuario.setString(7, cliente.getEstado().name());
                psUsuario.setString(8, cliente.getContraseniaHash());
                psUsuario.setLong(9, cliente.getId());
                psUsuario.executeUpdate();
            }

            try (PreparedStatement psCliente = conn.prepareStatement(updateCliente)) {
                if (cliente.getPedidoActivo() != null) {
                    psCliente.setLong(1, cliente.getPedidoActivo().getIdPedido());
                } else {
                    psCliente.setNull(1, java.sql.Types.INTEGER);
                }
                psCliente.setLong(2, cliente.getId());
                psCliente.executeUpdate();
            }

            TransactionContext.commit();
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
        return cliente;
    }

    @Override
    public void remove(Cliente cliente) throws SQLException {
        String deleteCliente = "DELETE FROM Cliente WHERE id_usuario = ?";
        String deleteUsuario = "DELETE FROM Usuario WHERE id_usuario = ?";

        Connection conn = TransactionContext.getConnection();

        try {
            try (PreparedStatement psCliente = conn.prepareStatement(deleteCliente)) {
                psCliente.setLong(1, cliente.getId());
                psCliente.executeUpdate();
            }

            try (PreparedStatement psUsuario = conn.prepareStatement(deleteUsuario)) {
                psUsuario.setLong(1, cliente.getId());
                psUsuario.executeUpdate();
            }

            TransactionContext.commit();
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    private Cliente mapResultSetToCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getLong("id_usuario"));
        c.setDni(rs.getString("dni"));
        c.setNombre(rs.getString("nombre"));
        c.setApellidoCompleto(rs.getString("apellido_completo"));
        c.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
        c.setFechaCreacionCuenta(rs.getDate("fecha_creacion_cuenta"));
        c.setCorreo(rs.getString("correo"));
        c.setContraseniaHash(rs.getString("contrasena_hash"));
        c.setTelefono(rs.getString("telefono"));
        c.setEstado(EstadoCuenta.valueOf(rs.getString("estado")));

        long idPedidoActivo = rs.getLong("id_pedido_activo");
        if (!rs.wasNull()) {
            Pedido pedido = new Pedido();
            pedido.setIdPedido(idPedidoActivo);
            c.setPedidoActivo(pedido);
        } else {
            c.setPedidoActivo(null);
        }

        return c;
    }
}