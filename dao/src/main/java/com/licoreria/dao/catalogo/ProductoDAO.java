package com.licoreria.dao.catalogo;


import com.licoreria.dao.BaseDAO;
import com.licoreria.dominio.catalogo.Categoria;
import com.licoreria.dominio.catalogo.Imagen;
import com.licoreria.dominio.catalogo.Producto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ProductoDAO extends BaseDAO<Producto, Integer> {
    void cargarImagen(Connection con, Producto producto, Imagen imagen) throws SQLException;

    void removerImagen(Connection con, Producto producto, Imagen imagen) throws SQLException;
    void removerImagen(Connection con, int idProducto, int idImagen) throws SQLException;

    void asignarImagenPrincipal(Connection con, Producto producto, Imagen imagen) throws SQLException;

    void asignarCategoria(Connection con, Producto producto, Categoria categoria) throws SQLException;

    void removerCategoria(Connection con, Producto producto, Categoria categoria) throws SQLException;

    List<Producto> getProductosPorCliente(Connection con, int idCliente) throws SQLException;
}