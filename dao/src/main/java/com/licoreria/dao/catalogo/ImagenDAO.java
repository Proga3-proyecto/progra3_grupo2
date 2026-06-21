package com.licoreria.dao.catalogo;

import com.licoreria.dao.BaseDAO;
import com.licoreria.dominio.catalogo.Imagen;
import com.licoreria.dominio.catalogo.Producto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ImagenDAO extends BaseDAO<Imagen,Integer> {
    Imagen get(Connection con, String url) throws SQLException;
    List<Imagen> getAllByProduct(Connection con, Producto producto) throws SQLException;
}
