package com.licoreria.dao.catalogo;

import com.licoreria.dao.BaseDAO;
import com.licoreria.dominio.catalogo.Categoria;
import com.licoreria.dominio.catalogo.Producto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CategoriaDAO extends BaseDAO<Categoria, Integer> {
    Categoria get(Connection con, String nombre) throws SQLException;
    List<Categoria> getAllByProducto(Connection con, Producto producto) throws SQLException;
}