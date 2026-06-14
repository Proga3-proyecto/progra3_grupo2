package com.licoreria.BusinessLayer.ProductoService;

import com.licoreria.dominio.productos.Producto;

import java.util.List;

public interface IProductoService  {
    Producto obtenerPorId(Long id);
    List<Producto> listarTodos();
    Producto crear(Producto producto);
    Producto actualizar(Producto producto);
    void eliminar(Producto producto);
    void actualizarStock(Long idProducto, int nuevoStock);
}