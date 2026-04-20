package com.licoreria.app.dao.productoDAO;

import com.licoreria.app.modelo.productos.Producto;

import java.util.List;

public interface ProductoDAO {
    Producto get(long id);
    List<Producto> getAll();
    void save(Producto producto);
    void update(Producto producto);
    void delete(Producto producto);
}