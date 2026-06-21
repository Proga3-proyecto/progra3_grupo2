package com.licoreria.dao.catalogo;


import com.licoreria.dao.BaseDAO;
import com.licoreria.dominio.catalogo.Categoria;
import com.licoreria.dominio.catalogo.Imagen;
import com.licoreria.dominio.catalogo.Producto;

import java.sql.Connection;
import java.sql.SQLException;
public interface ProductoDAO extends BaseDAO<Producto, Integer> {
    void cargarImagen(Connection con, Producto producto, Imagen imagen) throws SQLException;
    void removerImagen(Connection con, Producto producto, Imagen imagen) throws SQLException;
    void asignarImagenPrincipal(Connection con, Producto producto, Imagen imagen) throws SQLException;

    // Nuevas opciones para la gestión de relaciones con Categorías
    void asignarCategoria(Connection con, Producto producto, Categoria categoria) throws SQLException;
    void removerCategoria(Connection con, Producto producto, Categoria categoria) throws SQLException;
}