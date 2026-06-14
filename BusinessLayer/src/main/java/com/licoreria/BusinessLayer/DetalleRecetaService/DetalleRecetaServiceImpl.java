package com.licoreria.BusinessLayer.DetalleRecetaService;

import com.licoreria.BusinessLayer.util.CalculadoraImpuestos;
import com.licoreria.dao.DetalleRecetaDAO;
import com.licoreria.dao.ProductoDAO;
import com.licoreria.dao.RecetaDAO;
import com.licoreria.dao.impl.DetalleRecetaDAOImpl;
import com.licoreria.dao.impl.ProductoDAOImpl;
import com.licoreria.dao.impl.RecetaDAOImpl;
import com.licoreria.dominio.pedidos.DetalleElementoReceta;
import com.licoreria.dominio.pedidos.DetalleReceta;
import com.licoreria.dominio.productos.ElementoReceta;
import com.licoreria.dominio.productos.Producto;
import com.licoreria.dominio.productos.Receta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetalleRecetaServiceImpl  implements IDetalleRecetaService {
    private final DetalleRecetaDAO detalleRecetaDAO;
    private final RecetaDAO recetaDAO;
    private final ProductoDAO productoDAO;

    public DetalleRecetaServiceImpl() {
        this.detalleRecetaDAO = new DetalleRecetaDAOImpl();
        this.recetaDAO = new RecetaDAOImpl();
        this.productoDAO = new ProductoDAOImpl();
    }

    @Override
    public DetalleReceta obtenerPorId(Long id) {
        try {
            return detalleRecetaDAO.get(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener detalle de receta", e);
        }
    }

    @Override
    public List<DetalleReceta> listarTodos() {
        try {
            return detalleRecetaDAO.getAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar detalles de receta", e);
        }
    }

    @Override
    public DetalleReceta crearDesdeReceta(Receta receta, Long idPedido, Long idClienteCarrito) {
        Receta recetaCompleta;
        try {
            recetaCompleta = recetaDAO.get(receta.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Error al cargar receta completa", e);
        }

        DetalleReceta detalle = new DetalleReceta(recetaCompleta);
        List<DetalleElementoReceta> desglose = new ArrayList<>();
        for (ElementoReceta er : recetaCompleta.getElementos()) {
            DetalleElementoReceta der = new DetalleElementoReceta(er);
            der.setCantidadEspecifica((int) Math.ceil(er.getCantidad())); // o la cantidad exacta
            desglose.add(der);
        }
        detalle.setElementosDesglosados(desglose);
        recalcularMonto(detalle);

        try {
            return detalleRecetaDAO.save(detalle, idPedido, idClienteCarrito);
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar detalle de receta", e);
        }
    }

    @Override
    public DetalleReceta actualizar(DetalleReceta detalle) {
        recalcularMonto(detalle);
        try {
            return detalleRecetaDAO.update(detalle);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar detalle de receta", e);
        }
    }

    @Override
    public void eliminar(DetalleReceta detalle) {
        try {
            detalleRecetaDAO.remove(detalle);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar detalle de receta", e);
        }
    }

    @Override
    public List<DetalleReceta> obtenerPorPedido(Long idPedido) {
        try {
            return detalleRecetaDAO.getByPedido(idPedido);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener detalles por pedido", e);
        }
    }

    @Override
    public List<DetalleReceta> obtenerPorCarrito(Long idCliente) {
        try {
            return detalleRecetaDAO.getByCarrito(idCliente);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener detalles por carrito", e);
        }
    }

    @Override
    public void recalcularMonto(DetalleReceta detalle) {
        double subtotal = 0.0;
        double totalImpuestos = 0.0;
        for (DetalleElementoReceta der : detalle.getElementosDesglosados()) {
            ElementoReceta er = der.getElementoBase();
            Producto producto = er.getProducto();
            Producto prodCompleto;
            try {
                prodCompleto = productoDAO.get(producto.getId());
            } catch (SQLException e) {
                throw new RuntimeException("Error al cargar producto para recálculo", e);
            }
            int cantidadEspecifica = der.getCantidadEspecifica();
            double precioBase = prodCompleto.getPrecio();
            double descuento = prodCompleto.getDescuento();
            double precioConDescuento = precioBase - (precioBase * descuento / 100);
            double subtotalProducto = precioConDescuento * cantidadEspecifica;
            double impuestos = CalculadoraImpuestos.calcularTotalImpuestos(prodCompleto, cantidadEspecifica, precioConDescuento);
            subtotal += subtotalProducto;
            totalImpuestos += impuestos;
        }
        detalle.setMontoTotal(subtotal + totalImpuestos);
        detalle.setTotalImpuestos(totalImpuestos);
        detalle.setDescuentoTotal(0.0);
    }
}