package com.licoreria.dao.usuarios;

import com.licoreria.dao.BaseDAO;
import com.licoreria.dominio.usuarios.Admin;

import java.sql.Connection;
import java.sql.SQLException;

public interface AdminDAO extends BaseDAO<Admin, Integer> {
    Admin getPorCorreo(Connection con, String correo, String contrasena) throws SQLException;
}