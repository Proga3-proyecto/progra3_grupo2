package com.licoreria.dao.catalogo;

import com.licoreria.dao.BaseDAO;
import com.licoreria.dominio.catalogo.Marca;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface MarcaDAO extends BaseDAO<Marca, Integer> {
    Marca get(Connection con, String nombre) throws SQLException;
    Map<Integer, Marca> getAllByProductos(
            Connection con,
            List<Integer> idMarcas
    ) throws SQLException;
}
