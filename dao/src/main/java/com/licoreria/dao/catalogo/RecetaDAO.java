package com.licoreria.dao.catalogo;

import com.licoreria.dao.BaseDAO;
import com.licoreria.dominio.catalogo.Categoria;
import com.licoreria.dominio.catalogo.Imagen;
import com.licoreria.dominio.catalogo.Receta;

import java.sql.Connection;
import java.sql.SQLException;

public interface RecetaDAO extends BaseDAO<Receta, Integer> {
    void cargarImagen(Connection con, Receta receta, Imagen imagen) throws SQLException;

    void asignarImagenPrincipal(Connection con, Receta receta, Imagen imagen) throws SQLException;

    public void asignarCategoria(Connection con, Receta receta, Categoria categoria) throws SQLException;

    public void removerCategoria(Connection con, Receta receta, Categoria categoria) throws SQLException;
}