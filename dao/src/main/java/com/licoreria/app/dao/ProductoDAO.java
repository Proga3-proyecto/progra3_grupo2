package com.licoreria.app.dao;


import com.licoreria.app.dominio.pedidos.DetalleProducto;
import com.licoreria.app.dominio.pedidos.DetalleReceta;
import com.licoreria.app.dominio.productos.Producto;

import java.util.List;

public interface ProductoDAO {
    Producto get(long id);
    List<Producto> getAll();
    void save(Producto producto);
    void update(Producto producto);
    void delete(Producto producto);




}