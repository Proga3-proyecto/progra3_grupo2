package com.licoreria.dao.catalogo;

import com.licoreria.dao.BaseDAO;
import com.licoreria.dominio.catalogo.Categoria;
import com.licoreria.dominio.catalogo.Producto;
import com.licoreria.dominio.catalogo.Receta;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CategoriaDAO extends BaseDAO<Categoria, Integer> {
    Categoria get(Connection con, String nombre) throws SQLException;

    List<Categoria> getAllByProducto(Connection con, Producto producto) throws SQLException;

    List<Categoria> getAllByReceta(Connection con, Receta receta) throws SQLException;

    Map<Integer, List<Categoria>> getAllByProductos(
            Connection con,
            List<Integer> idsProductos
    ) throws SQLException;

    Map<Integer, List<Categoria>> getAllByRecetas(
            Connection con,
            List<Integer> idsRecetas
    ) throws SQLException;
}