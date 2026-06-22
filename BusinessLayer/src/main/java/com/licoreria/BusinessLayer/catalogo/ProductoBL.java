package com.licoreria.BusinessLayer.catalogo;

import com.licoreria.dominio.catalogo.Categoria;
import com.licoreria.dominio.catalogo.Marca;
import com.licoreria.dominio.catalogo.Producto;

import java.util.List;

public interface ProductoBL {
    List<Producto> getAll();
    Producto get(int id);
    void delete(Producto producto);
    Producto save(Producto producto);
    Producto update(Producto producto);
    void agregarImagen(Producto producto,String url);
    void agregarImagenPrincipal(Producto producto,String url);
    void eliminarCategoria(Producto producto, String categoria);
    void agregarCategoria(Producto producto, Categoria categoria);
    void actualizarMarca(Producto producto, Marca categoria);
}
