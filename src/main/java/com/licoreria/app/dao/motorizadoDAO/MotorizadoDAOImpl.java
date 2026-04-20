package com.licoreria.app.dao.motorizadoDAO;

import com.licoreria.app.dao.conexionBD.ConexionDB;
import com.licoreria.app.modelo.usuarios.EstadoCuenta;
import com.licoreria.app.modelo.usuarios.Motorizado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MotorizadoDAOImpl implements MotorizadoDAO {

    @Override
    public Motorizado get(long id) {
        Motorizado motorizado = null;
        String query = "SELECT u.*, m.placa, m.horas_trabajo, m.pago_mensual FROM Usuario u INNER JOIN Motorizado m ON u.id_usuario = m.id_usuario WHERE u.id_usuario = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    motorizado = mapResultSetToMotorizado(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return motorizado;
    }

    @Override
    public List<Motorizado> getAll() {
        List<Motorizado> motorizados = new ArrayList<>();
        String query = "SELECT u.*, m.placa, m.horas_trabajo, m.pago_mensual FROM Usuario u INNER JOIN Motorizado m ON u.id_usuario = m.id_usuario";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                motorizados.add(mapResultSetToMotorizado(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return motorizados;
    }

    @Override
    public void save(Motorizado motorizado) {
        String insertUsuario = "INSERT INTO Usuario (dni, nombre, apellido_completo, fecha_nacimiento, fecha_creacion_cuenta, correo, contrasena_hash, telefono, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertMotorizado = "INSERT INTO Motorizado (id_usuario, placa, horas_trabajo, pago_mensual) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = ConexionDB.getConexion();
            conn.setAutoCommit(false);

            try (PreparedStatement psUsuario = conn.prepareStatement(insertUsuario, Statement.RETURN_GENERATED_KEYS)) {
                psUsuario.setString(1, motorizado.getDni());
                psUsuario.setString(2, motorizado.getNombre());
                psUsuario.setString(3, motorizado.getApellidoCompleto());
                psUsuario.setDate(4, motorizado.getFechaNacimiento() != null ? new java.sql.Date(motorizado.getFechaNacimiento().getTime()) : null);


                psUsuario.setDate(5, motorizado.getFechaCreacionCuenta() != null ? new java.sql.Date(motorizado.getFechaCreacionCuenta().getTime()) : new java.sql.Date(System.currentTimeMillis()));

                psUsuario.setString(6, motorizado.getCorreo());
                psUsuario.setString(7, motorizado.getContraseniaHash());
                psUsuario.setString(8, motorizado.getTelefono());

               // String estado = (motorizado.getEstadoString() != null) ? motorizado.getEstadoString() : "ACTIVA";
                psUsuario.setString(9, motorizado.getEstado().name());

                int affectedRows = psUsuario.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Fallo al crear el usuario motorizado, no se afectaron filas.");
                }

                // Obtener el ID generado para Usuario
                try (ResultSet generatedKeys = psUsuario.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long idGenerado = generatedKeys.getLong(1);
                        motorizado.setId(idGenerado); // Actualizar ID en el objeto

                        // 2. Insertar en tabla Hija (Motorizado)
                        try (PreparedStatement psMotorizado = conn.prepareStatement(insertMotorizado)) {
                            psMotorizado.setLong(1, idGenerado);
                            psMotorizado.setString(2, motorizado.getPlaca());
                            psMotorizado.setDouble(3, motorizado.getHorasTrabajo());
                            psMotorizado.setDouble(4, motorizado.getPagoMensual());
                            psMotorizado.executeUpdate();
                        }
                    } else {
                        throw new SQLException("Fallo al crear el motorizado, no se obtuvo el ID de Usuario.");
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
    public void update(Motorizado motorizado) {
        String updateUsuario = "UPDATE Usuario SET dni=?, nombre=?, apellido_completo=?, fecha_nacimiento=?, correo=?, telefono=?, estado=?, contrasena_hash=? WHERE id_usuario=?";
        String updateMotorizado = "UPDATE Motorizado SET placa=?, horas_trabajo=?, pago_mensual=? WHERE id_usuario=?";

        Connection conn = null;
        try {
            conn = ConexionDB.getConexion();
            conn.setAutoCommit(false);

            try (PreparedStatement psUsuario = conn.prepareStatement(updateUsuario)) {
                psUsuario.setString(1, motorizado.getDni());
                psUsuario.setString(2, motorizado.getNombre());
                psUsuario.setString(3, motorizado.getApellidoCompleto());
                psUsuario.setDate(4, motorizado.getFechaNacimiento() != null ? new java.sql.Date(motorizado.getFechaNacimiento().getTime()) : null);
                psUsuario.setString(5, motorizado.getCorreo());
                psUsuario.setString(6, motorizado.getTelefono());

                psUsuario.setString(7, motorizado.getEstado().name());
                psUsuario.setString(8, motorizado.getContraseniaHash());
                psUsuario.setLong(9, motorizado.getId());

                psUsuario.executeUpdate();
            }

            // 2. Actualizar Motorizado
            try (PreparedStatement psMotorizado = conn.prepareStatement(updateMotorizado)) {
                psMotorizado.setString(1, motorizado.getPlaca());
                psMotorizado.setDouble(2, motorizado.getHorasTrabajo());
                psMotorizado.setDouble(3, motorizado.getPagoMensual());
                psMotorizado.setLong(4, motorizado.getId());
                psMotorizado.executeUpdate();
            }

            conn.commit(); // Confirmar transacción
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
    public void delete(Motorizado motorizado) {
        String deleteUsuario = "DELETE FROM Usuario WHERE id_usuario = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(deleteUsuario)) {

            ps.setLong(1, motorizado.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private Motorizado mapResultSetToMotorizado(ResultSet rs) throws SQLException {
        Motorizado m = new Motorizado(
                rs.getString("dni"),
                rs.getString("nombre"),
                rs.getString("correo"),
                rs.getString("telefono"),
                rs.getString("apellido_completo"),
                rs.getDate("fecha_nacimiento"),
                rs.getString("contrasena_hash"),
                rs.getString("placa"),
                rs.getDouble("horas_trabajo"),
                rs.getDouble("pago_mensual")
        );

        m.setId(rs.getLong("id_usuario"));

        java.sql.Date fechaCrea = rs.getDate("fecha_creacion_cuenta");
        if (!rs.wasNull()) {
            m.setFechaCreacionCuenta(new java.util.Date(fechaCrea.getTime()));
        }

        m.setEstado(EstadoCuenta.valueOf(rs.getString("estado")));

        return m;
    }
}