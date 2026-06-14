package com.licoreria.BusinessLayer.ProductoService;

import com.licoreria.dao.ProductoDAO;
import com.licoreria.dao.impl.ProductoDAOImpl;
import com.licoreria.dominio.productos.Producto;

import java.sql.SQLException;
import java.util.List;

public class ProductoServiceImpl  implements IProductoService {
    private final ProductoDAO productoDAO;

    public ProductoServiceImpl() {
        this.productoDAO = new ProductoDAOImpl();
    }

    @Override
    public Producto obtenerPorId(Long id) {
        try {
            return productoDAO.get(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Producto> listarTodos() {
        try {
            return productoDAO.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Producto crear(Producto producto) {
        try {
            return productoDAO.save(producto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Producto actualizar(Producto producto) {
        try {
            return productoDAO.update(producto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Producto producto) {
        try {
            productoDAO.remove(producto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actualizarStock(Long idProducto, int nuevoStock) {
        Producto p = obtenerPorId(idProducto);
        p.setStock(nuevoStock);
        actualizar(p);
    }
}