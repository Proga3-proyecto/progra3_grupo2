package com.licoreria.app.dao.adminDAO;

import com.licoreria.app.dao.conexionBD.ConexionDB;
import com.licoreria.app.modelo.usuarios.Admin;
import com.licoreria.app.modelo.usuarios.EstadoCuenta;
import com.licoreria.app.utils.DateUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAOImpl implements AdminDAO {

    @Override
    public Admin get(long id) {
        Admin admin = null;
        String query = "SELECT u.*, a.fecha_inicio_admin FROM Usuario u INNER JOIN Admin a ON u.id_usuario = a.id_usuario WHERE u.id_usuario = ?";

        try (Connection conn = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    admin = mapResultSetToAdmin(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }

    @Override
    public List<Admin> getAll() {
        List<Admin> administradores = new ArrayList<>();
        String query = "SELECT u.*, a.fecha_inicio_admin FROM Usuario u INNER JOIN Admin a ON u.id_usuario = a.id_usuario";

        try (Connection conn = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                administradores.add(mapResultSetToAdmin(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return administradores;
    }

    @Override
    public void save(Admin admin) {
        String insertUsuario = "INSERT INTO Usuario (dni, nombre, apellido_completo, fecha_nacimiento, fecha_creacion_cuenta, correo, contrasena_hash, telefono, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertAdmin = "INSERT INTO Admin (id_usuario, fecha_inicio_admin) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = ConexionDB.getInstance().getConexion();
            conn.setAutoCommit(false);

            try (PreparedStatement psUsuario = conn.prepareStatement(insertUsuario, Statement.RETURN_GENERATED_KEYS)) {
                psUsuario.setString(1, admin.getDni());
                psUsuario.setString(2, admin.getNombre());
                psUsuario.setString(3, admin.getApellidoCompleto());
                psUsuario.setDate(4, DateUtils.toSqlDate(admin.getFechaNacimiento()));
                psUsuario.setDate(5, DateUtils.toSqlDate(admin.getFechaCreacionCuenta(), System.currentTimeMillis()));
                psUsuario.setString(6, admin.getCorreo());
                psUsuario.setString(7, admin.getContraseniaHash());
                psUsuario.setString(8, admin.getTelefono());
                psUsuario.setString(9, admin.getEstado().name());

                int affectedRows = psUsuario.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Fallo al crear el administrador, no se afectaron filas en Usuario.");
                }

                try (ResultSet generatedKeys = psUsuario.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long idGenerado = generatedKeys.getLong(1);
                        admin.setId(idGenerado);
                        try (PreparedStatement psAdmin = conn.prepareStatement(insertAdmin)) {
                            psAdmin.setLong(1, idGenerado);
                            if (admin.getFechaInicioAdmin() != null) {
                                psAdmin.setDate(2, DateUtils.toSqlDate(admin.getFechaInicioAdmin()));
                            } else {
                                psAdmin.setNull(2, java.sql.Types.DATE);
                            }
                            psAdmin.executeUpdate();
                        }
                    } else {
                        throw new SQLException("Fallo al crear el administrador, no se obtuvo el ID de Usuario.");
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
    public void update(Admin admin) {
        String updateUsuario = "UPDATE Usuario SET dni=?, nombre=?, apellido_completo=?, fecha_nacimiento=?, correo=?, telefono=?, estado=?, contrasena_hash=? WHERE id_usuario=?";
        String updateAdmin = "UPDATE Admin SET fecha_inicio_admin=? WHERE id_usuario=?";

        Connection conn = null;
        try {
            conn = ConexionDB.getInstance().getConexion();
            conn.setAutoCommit(false);

            try (PreparedStatement psUsuario = conn.prepareStatement(updateUsuario)) {
                psUsuario.setString(1, admin.getDni());
                psUsuario.setString(2, admin.getNombre());
                psUsuario.setString(3, admin.getApellidoCompleto());
                psUsuario.setDate(4, DateUtils.toSqlDate(admin.getFechaNacimiento()));
                psUsuario.setString(5, admin.getCorreo());
                psUsuario.setString(6, admin.getTelefono());
                psUsuario.setString(7, admin.getEstado().name());
                psUsuario.setString(8, admin.getContraseniaHash());
                psUsuario.setLong(9, admin.getId());

                psUsuario.executeUpdate();
            }

            try (PreparedStatement psAdmin = conn.prepareStatement(updateAdmin)) {
                if (admin.getFechaInicioAdmin() != null) {
                    psAdmin.setDate(1, DateUtils.toSqlDate(admin.getFechaInicioAdmin()));
                } else {
                    psAdmin.setNull(1, java.sql.Types.DATE);
                }
                psAdmin.setLong(2, admin.getId());
                psAdmin.executeUpdate();
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
    public void delete(Admin admin) {
        String deleteUsuario = "DELETE FROM Usuario WHERE id_usuario = ?";
        try (Connection conn = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = conn.prepareStatement(deleteUsuario)) {

            ps.setLong(1, admin.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private Admin mapResultSetToAdmin(ResultSet rs) throws SQLException {
        Admin admin = new Admin(
                rs.getString("dni"),
                rs.getString("nombre"),
                rs.getString("correo"),
                rs.getString("telefono"),
                rs.getString("apellido_completo"),
                rs.getDate("fecha_nacimiento"),
                rs.getString("contrasena_hash")
        );

        admin.setId(rs.getLong("id_usuario"));

        Date fechaCrea = rs.getDate("fecha_creacion_cuenta");
        if (!rs.wasNull()) {
            admin.setFechaInicioAdmin(DateUtils.toDate(fechaCrea.getTime()));
        }

        Date fechaInicio = rs.getDate("fecha_inicio_admin");
        if (!rs.wasNull()) {
            admin.setFechaInicioAdmin(DateUtils.toDate(fechaInicio.getTime()));
        }

        return admin;
    }
}