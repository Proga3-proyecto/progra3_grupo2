package com.licoreria.dao.catalogo;

import com.licoreria.dao.BaseDAO;
import com.licoreria.dominio.catalogo.Marca;

import java.sql.Connection;
import java.sql.SQLException;

public interface MarcaDAO extends BaseDAO<Marca, Integer> {
    Marca get(Connection con, String nombre) throws SQLException;
}
