package com.licoreria.dao.usuarios;

import com.licoreria.dao.BaseDAO;
import com.licoreria.dominio.usuarios.Cliente;

import java.sql.Connection;
import java.sql.SQLException;

public interface ClienteDAO extends BaseDAO<Cliente, Integer> {
    Cliente getPorCorreo(Connection con, String correo, String contrasena) throws SQLException;
}