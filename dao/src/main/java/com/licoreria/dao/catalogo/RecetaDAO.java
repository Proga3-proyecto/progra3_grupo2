package com.licoreria.dao.catalogo;

import com.licoreria.dao.BaseDAO;
import com.licoreria.dominio.catalogo.Categoria;
import com.licoreria.dominio.catalogo.Imagen;
import com.licoreria.dominio.catalogo.Receta;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface RecetaDAO extends BaseDAO<Receta, Integer> {
    void cargarImagen(Connection con, Receta receta, Imagen imagen) throws SQLException;

    void asignarImagenPrincipal(Connection con, Receta receta, Imagen imagen) throws SQLException;

    void asignarCategoria(Connection con, Receta receta, Categoria categoria) throws SQLException;

    void removerCategoria(Connection con, Receta receta, Categoria categoria) throws SQLException;

    List<Receta> getRecetasPorCliente(Connection con, int idCliente) throws SQLException;

    Map<Integer, Receta> getMapByIds(Connection con, List<Integer> idsRecetas) throws SQLException;


}