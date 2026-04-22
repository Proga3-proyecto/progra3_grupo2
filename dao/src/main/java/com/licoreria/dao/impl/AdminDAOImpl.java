package com.licoreria.dao.impl;

import com.licoreria.DBmanager.DBManager;
import com.licoreria.DBmanager.TransactionContext;
import com.licoreria.dao.AdminDAO;
import com.licoreria.dao.utils.DateUtils;
import com.licoreria.dominio.usuarios.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAOImpl implements AdminDAO {

    @Override
    public Admin get(Long id) throws SQLException {
        Admin admin = null;
        String query = "SELECT u.*, a.fecha_inicio_admin FROM Usuario u INNER JOIN Admin a ON u.id_usuario = a.id_usuario WHERE u.id_usuario = ?";

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    admin = mapResultSetToAdmin(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return admin;
    }


    @Override
    public List<Admin> getAll() throws SQLException {
        List<Admin> administradores = new ArrayList<>();
        String query = "SELECT u.*, a.fecha_inicio_admin FROM Usuario u INNER JOIN Admin a ON u.id_usuario = a.id_usuario";

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                administradores.add(mapResultSetToAdmin(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return administradores;
    }



    @Override
    public Admin save(Admin admin) throws SQLException {
        String insertUsuario = "INSERT INTO Usuario (dni, nombre, apellido_completo, fecha_nacimiento, fecha_creacion_cuenta, correo, contrasena_hash, telefono, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertAdmin = "INSERT INTO Admin (id_usuario, fecha_inicio_admin) VALUES (?, ?)";
        Connection conn = TransactionContext.getConnection();

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
            TransactionContext.commit();
        }catch (SQLException e){
            TransactionContext.rollback();
            throw new RuntimeException(e);
        }finally {
            TransactionContext.close();
        }
        return admin;

    }

    @Override
    public Admin update(Admin admin) throws SQLException {
        String updateUsuario = "UPDATE Usuario SET dni=?, nombre=?, apellido_completo=?, fecha_nacimiento=?, correo=?, telefono=?, estado=?, contrasena_hash=? WHERE id_usuario=?";
        String updateAdmin = "UPDATE Admin SET fecha_inicio_admin=? WHERE id_usuario=?";

        Connection conn = TransactionContext.getConnection();

        try {
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

            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new RuntimeException("Error al actualizar el administrador: " + e.getMessage(), e);
        } finally {
            TransactionContext.close();
        }

        return admin;
    }

    @Override
    public void remove(Admin admin) throws SQLException {
        String deleteAdmin = "DELETE FROM Admin WHERE id_usuario = ?";
        String deleteUsuario = "DELETE FROM Usuario WHERE id_usuario = ?";

        Connection conn = TransactionContext.getConnection();

        try {
            try (PreparedStatement psAdmin = conn.prepareStatement(deleteAdmin)) {
                psAdmin.setLong(1, admin.getId());
                psAdmin.executeUpdate();
            }
            try (PreparedStatement psUsuario = conn.prepareStatement(deleteUsuario)) {
                psUsuario.setLong(1, admin.getId());
                psUsuario.executeUpdate();
            }
            TransactionContext.commit();

        } catch (SQLException e) {
            TransactionContext.rollback();
            throw new RuntimeException("Error al eliminar el administrador: " + e.getMessage(), e);
        } finally {
            TransactionContext.close();
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