package com.licoreria.app.dao.clienteDAO;

import com.licoreria.app.dao.conexionBD.ConexionDB;
import com.licoreria.app.modelo.pedidos.Pedido;
import com.licoreria.app.modelo.usuarios.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {
    @Override
    public Cliente get(long id) {
        Cliente cliente = null;
        String query = "SELECT u.*, c.id_pedido_activo FROM Usuario u INNER JOIN Cliente c ON u.id_usuario = c.id_usuario WHERE u.id_usuario = ?";

        try (Connection conn = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cliente = mapResultSetToCliente(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }

    @Override
    public List<Cliente> getAll() {
        List<Cliente> clientes = new ArrayList<>();
        String query = "SELECT u.*, c.id_pedido_activo FROM Usuario u INNER JOIN Cliente c ON u.id_usuario = c.id_usuario";

        try (Connection conn = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clientes.add(mapResultSetToCliente(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    @Override
    public void save(Cliente cliente) {
        String insertUsuario = "INSERT INTO Usuario (dni, nombre, apellido_completo, fecha_nacimiento, fecha_creacion_cuenta, correo, contrasena_hash, telefono, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertCliente = "INSERT INTO Cliente (id_usuario, id_pedido_activo) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = ConexionDB.getInstance().getConexion();
            conn.setAutoCommit(false);

            try (PreparedStatement psUsuario = conn.prepareStatement(insertUsuario, Statement.RETURN_GENERATED_KEYS)) {
                psUsuario.setString(1, cliente.getDni());
                psUsuario.setString(2, cliente.getNombre());
                psUsuario.setString(3, cliente.getApellidoCompleto());
                psUsuario.setDate(4, new java.sql.Date(cliente.getFechaNacimiento().getTime()));
                psUsuario.setDate(5, new java.sql.Date(cliente.getFechaCreacionCuenta().getTime()));
                psUsuario.setString(6, cliente.getCorreo());
                psUsuario.setString(7, cliente.getContraseniaHash());
                psUsuario.setString(8, cliente.getTelefono());
                psUsuario.setString(9, cliente.getEstado() != null ? cliente.getEstadoString() : "ACTIVA");

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
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void update(Cliente cliente) {
        String updateUsuario = "UPDATE Usuario SET dni=?, nombre=?, apellido_completo=?, fecha_nacimiento=?, correo=?, telefono=?, estado=?, contrasena_hash=? WHERE id_usuario=?";
        String updateCliente = "UPDATE Cliente SET id_pedido_activo=? WHERE id_usuario=?";

        Connection conn = null;
        try {
            conn = ConexionDB.getInstance().getConexion();
            conn.setAutoCommit(false);

            try (PreparedStatement psUsuario = conn.prepareStatement(updateUsuario)) {
                psUsuario.setString(1, cliente.getDni());
                psUsuario.setString(2, cliente.getNombre());
                psUsuario.setString(3, cliente.getApellidoCompleto());
                psUsuario.setDate(4, new java.sql.Date(cliente.getFechaNacimiento().getTime()));
                psUsuario.setString(5, cliente.getCorreo());
                psUsuario.setString(6, cliente.getTelefono());
                psUsuario.setString(7, cliente.getEstadoString());
                psUsuario.setLong(8, cliente.getId());
                psUsuario.setString(9, cliente.getContraseniaHash());
                psUsuario.executeUpdate();
            }

            // 2. Actualizar Cliente
            try (PreparedStatement psCliente = conn.prepareStatement(updateCliente)) {
                if (cliente.getPedidoActivo() != null) {
                    psCliente.setLong(1, cliente.getPedidoActivo().getIdPedido());
                } else {
                    psCliente.setNull(1, java.sql.Types.INTEGER);
                }
                psCliente.setLong(2, cliente.getId());
                psCliente.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void delete(Cliente cliente) {
        String deleteUsuario = "DELETE FROM Usuario WHERE id_usuario = ?";
        try (Connection conn = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = conn.prepareStatement(deleteUsuario)) {
            ps.setLong(1, cliente.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Cliente mapResultSetToCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getLong("id_usuario"));
        c.setDni(rs.getString("dni"));
        c.setNombre(rs.getString("nombre"));
        c.setApellidoCompleto(rs.getString("apellido_completo"));
        c.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
        c.setFechaCreacionCuenta(rs.getDate("fecha_creacion_cuenta")); // Corregido: nombre correcto de la columna
        c.setCorreo(rs.getString("correo"));
        c.setContraseniaHash(rs.getString("contrasena_hash"));
        c.setTelefono(rs.getString("telefono"));
        c.setEstado(rs.getString("estado"));

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
