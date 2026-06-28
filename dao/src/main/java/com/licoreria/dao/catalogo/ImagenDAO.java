package com.licoreria.dao.catalogo;

import com.licoreria.dao.BaseDAO;
import com.licoreria.dominio.catalogo.Imagen;
import com.licoreria.dominio.catalogo.Producto;
import com.licoreria.dominio.catalogo.Receta;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ImagenDAO extends BaseDAO<Imagen, Integer> {
    Imagen get(Connection con, String url) throws SQLException;

    List<Imagen> getAllByProduct(Connection con, Producto producto) throws SQLException;

    List<Imagen> getAllByReceta(Connection con, Receta receta) throws SQLException;

    Map<Integer, List<Imagen>> getAllByProducts(
            Connection con,
            List<Integer> idsProducto
    ) throws SQLException;

    Map<Integer, List<Imagen>> getAllByRecetas(
            Connection con,
            List<Integer> idsProducto
    ) throws SQLException;

    Map<Integer,Imagen> getMapByIds(
            Connection con,
            List<Integer> ids
    ) throws SQLException;

}
