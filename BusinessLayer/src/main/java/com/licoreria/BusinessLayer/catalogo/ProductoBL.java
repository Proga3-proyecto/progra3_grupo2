package com.licoreria.BusinessLayer.catalogo;

import com.licoreria.dominio.catalogo.Categoria;
import com.licoreria.dominio.catalogo.Imagen;
import com.licoreria.dominio.catalogo.Marca;
import com.licoreria.dominio.catalogo.Producto;

import java.util.List;

public interface ProductoBL {
    List<Producto> getAll();
    Producto get(int id);
    void delete(Producto producto);
    void delete(int id);
    Producto save(Producto producto);
    Producto update(Producto producto);
    Imagen agregarImagen(Producto producto, String url);
    void agregarImagenPrincipal(Producto producto,int idImagen);
    void removerImagen(int idProducto, int imagen);
    void eliminarCategoria(Producto producto, String categoria);
    void agregarCategoria(Producto producto, Categoria categoria);
    void actualizarMarca(Producto producto, Marca categoria);
}
