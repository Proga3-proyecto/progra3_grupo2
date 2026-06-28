package com.licoreria.dao.usuarios;

import com.licoreria.dao.DAOUtils;
import com.licoreria.dominio.usuarios.Admin;
import com.licoreria.dominio.usuarios.EstadoUsuario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AdminDAOImpl implements AdminDAO {

    @Override
    public Admin get(Connection con, Integer id) throws SQLException {
        final String sql = "SELECT u.id_usuario, u.dni, u.nombre, u.apellido_completo, u.correo, u.contrasena_hash, u.estado, " +
                "a.fecha_inicio_admin, a.is_master" +
                "FROM Admin a " +
                "INNER JOIN Usuario u ON a.id_usuario = u.id_usuario " +
                "WHERE a.id_usuario = ?";

        return DAOUtils.get(sql, con, (ps) -> ps.setInt(1, id), this::mapearAdmin);
    }

    @Override
    public List<Admin> getAll(Connection con) throws SQLException {
        final String sql = "SELECT u.id_usuario, u.dni, u.nombre, u.apellido_completo, u.correo, u.contrasena_hash, u.estado, " +
                "a.fecha_inicio_admin, a.is_master " +
                "FROM Admin a " +
                "INNER JOIN Usuario u ON a.id_usuario = u.id_usuario";

        return DAOUtils.getAll(sql, con, this::mapearAdmin);
    }

    @Override
    public Admin save(Connection con, Admin admin) throws SQLException {
        final String sqlUsuario = "INSERT INTO Usuario (dni, nombre, apellido_completo, correo, contrasena_hash, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        DAOUtils.save(sqlUsuario, con, (ps) -> {
            ps.setString(1, admin.getDni());
            ps.setString(2, admin.getNombre());
            ps.setString(3, admin.getApellidoCompleto());
            ps.setString(4, admin.getCorreo());
            ps.setString(5, admin.getContrasenaHash());
            ps.setString(6, admin.getEstado().name());
        }, (rs) -> {
            admin.setIdUsuario(rs.getInt(1)); // Capturamos el ID autogenerado
        });

        final String sqlAdmin = "INSERT INTO Admin (id_usuario, fecha_inicio_admin) VALUES (?, ?)";

        DAOUtils.save(sqlAdmin, con, (ps) -> {
            ps.setInt(1, admin.getIdUsuario());
            ps.setDate(2, new java.sql.Date(admin.getFechaInicioAdmin().getTime()));
        }, (rs) -> {
        });

        return admin;
    }

    @Override
    public Admin update(Connection con, Admin admin) throws SQLException {
        final String sqlUsuario = "UPDATE Usuario SET dni = ?, nombre = ?, apellido_completo = ?, correo = ?, " +
                "contrasena_hash = ?, estado = ? WHERE id_usuario = ?";
        DAOUtils.update(sqlUsuario, con, (ps) -> {
            ps.setString(1, admin.getDni());
            ps.setString(2, admin.getNombre());
            ps.setString(3, admin.getApellidoCompleto());
            ps.setString(4, admin.getCorreo());
            ps.setString(5, admin.getContrasenaHash());
            ps.setString(6, admin.getEstado().name());
            ps.setInt(7, admin.getIdUsuario());
        });

        final String sqlAdmin = "UPDATE Admin SET fecha_inicio_admin = ? WHERE id_usuario = ?";
        DAOUtils.update(sqlAdmin, con, (ps) -> {
            ps.setDate(1, new java.sql.Date(admin.getFechaInicioAdmin().getTime()));
            ps.setInt(2, admin.getIdUsuario());
        });

        return admin;
    }

    @Override
    public void remove(Connection con, Admin admin) throws SQLException {
        final String sql = "DELETE FROM Usuario WHERE id_usuario = ?";
        DAOUtils.delete(sql, con, (ps) -> {
            ps.setInt(1, admin.getIdUsuario());
        });
    }


    private Admin mapearAdmin(java.sql.ResultSet rs) throws SQLException {
        Admin admin = new Admin();
        admin.setIdUsuario(rs.getInt("id_usuario"));
        admin.setDni(rs.getString("dni"));
        admin.setNombre(rs.getString("nombre"));
        admin.setApellidoCompleto(rs.getString("apellido_completo"));
        admin.setCorreo(rs.getString("correo"));
        admin.setContrasenaHash(rs.getString("contrasena_hash"));
        admin.setEstado(EstadoUsuario.valueOf(rs.getString("estado")));
        admin.setFechaInicioAdmin(rs.getDate("fecha_inicio_admin"));
        admin.setMaster(rs.getBoolean("is_master"));
        return admin;
    }

    @Override
    public Admin getPorCorreo(Connection con, String correo, String contrasena) throws SQLException {
        final String sql = "SELECT u.id_usuario, u.dni, u.nombre, u.apellido_completo, u.correo, u.contrasena_hash, u.estado, " +
                "a.fecha_inicio_admin,  a.is_master " +
                "FROM Admin a " +
                "INNER JOIN Usuario u ON a.id_usuario = u.id_usuario " +
                "WHERE u.correo = ? AND u.contrasena_hash = ?";

        return DAOUtils.get(sql, con, (ps) -> {
            ps.setString(1, correo);
            ps.setString(2, contrasena);
        }, this::mapearAdmin);
    }
}