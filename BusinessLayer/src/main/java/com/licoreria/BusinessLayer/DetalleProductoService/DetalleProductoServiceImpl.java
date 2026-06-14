package com.licoreria.BusinessLayer.DetalleProductoService;

import com.licoreria.BusinessLayer.util.CalculadoraImpuestos;
import com.licoreria.dao.DetalleProductoDAO;
import com.licoreria.dao.ProductoDAO;
import com.licoreria.dao.impl.DetalleProductoDAOImpl;
import com.licoreria.dao.impl.ProductoDAOImpl;
import com.licoreria.dominio.pedidos.DetalleProducto;
import com.licoreria.dominio.productos.Producto;

import java.sql.SQLException;
import java.util.List;

public class DetalleProductoServiceImpl implements IDetalleProductoService {
    private final DetalleProductoDAO detalleProductoDAO;
    private final ProductoDAO productoDAO;

    public DetalleProductoServiceImpl() {
        this.detalleProductoDAO = new DetalleProductoDAOImpl();
        this.productoDAO = new ProductoDAOImpl();
    }

    @Override
    public DetalleProducto obtenerPorId(Long id) {
        try {
            return detalleProductoDAO.get(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener detalle de producto", e);
        }
    }

    @Override
    public List<DetalleProducto> listarTodos() {
        try {
            return detalleProductoDAO.getAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar detalles de producto", e);
        }
    }

    @Override
    public DetalleProducto crearEnPedido(DetalleProducto detalle, Long idPedido) {
        recalcularMonto(detalle);
        try {
            return detalleProductoDAO.save(detalle, idPedido, null);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear detalle en pedido", e);
        }
    }

    @Override
    public DetalleProducto crearEnCarrito(DetalleProducto detalle, Long idCliente) {
        recalcularMonto(detalle);
        try {
            return detalleProductoDAO.save(detalle, null, idCliente);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear detalle en carrito", e);
        }
    }

    @Override
    public DetalleProducto actualizar(DetalleProducto detalle) {
        recalcularMonto(detalle);
        try {
            return detalleProductoDAO.update(detalle);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar detalle", e);
        }
    }

    @Override
    public void eliminar(DetalleProducto detalle) {
        try {
            detalleProductoDAO.remove(detalle);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar detalle", e);
        }
    }

    @Override
    public List<DetalleProducto> obtenerPorPedido(Long idPedido) {
        try {
            return detalleProductoDAO.getByPedido(idPedido);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener detalles por pedido", e);
        }
    }

    @Override
    public List<DetalleProducto> obtenerPorCarrito(Long idCliente) {
        try {
            return detalleProductoDAO.getByCarrito(idCliente);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener detalles por carrito", e);
        }
    }

    @Override
    public void recalcularMonto(DetalleProducto detalle) {
        Producto producto;
        try {
            producto = productoDAO.get(detalle.getProducto().getId());
        } catch (SQLException e) {
            throw new RuntimeException("Error al cargar producto para recalcular", e);
        }
        double precioBase = producto.getPrecio();
        double descuento = producto.getDescuento();
        int cantidad = detalle.getCantidad();
        double precioConDescuento = precioBase - (precioBase * descuento / 100);
        double subtotal = precioConDescuento * cantidad;
        double impuestos = CalculadoraImpuestos.calcularTotalImpuestos(producto, cantidad, precioConDescuento);
        detalle.setMontoTotal(subtotal + impuestos);
        detalle.setDescuentoTotal(precioBase * cantidad - precioConDescuento * cantidad);
        detalle.setTotalImpuestos(impuestos);
    }
}