package com.licoreria.BusinessLayer.PedidoService;

import com.licoreria.BusinessLayer.DetalleRecetaService.DetalleRecetaServiceImpl;
import com.licoreria.BusinessLayer.DetalleRecetaService.IDetalleRecetaService;
import com.licoreria.BusinessLayer.util.CalculadoraImpuestos;
import com.licoreria.dao.DetalleProductoDAO;
import com.licoreria.dao.DetalleRecetaDAO;
import com.licoreria.dao.PedidoDAO;
import com.licoreria.dao.impl.DetalleProductoDAOImpl;
import com.licoreria.dao.impl.DetalleRecetaDAOImpl;
import com.licoreria.dao.impl.PedidoDAOImpl;
import com.licoreria.dominio.pedidos.*;
import com.licoreria.dominio.productos.Producto;
import com.licoreria.dominio.usuarios.Cliente;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class PedidoServiceImpl implements IPedidoService {
    private final PedidoDAO pedidoDAO;
    private final DetalleProductoDAO detalleProductoDAO;
    private final DetalleRecetaDAO detalleRecetaDAO;
    private final IDetalleRecetaService detalleRecetaService;
    public PedidoServiceImpl() {
        this.pedidoDAO = new PedidoDAOImpl();
        this.detalleProductoDAO = new DetalleProductoDAOImpl();
        this.detalleRecetaDAO = new DetalleRecetaDAOImpl();
        this.detalleRecetaService = new DetalleRecetaServiceImpl();
    }

    @Override
    public Pedido obtenerPorId(Long id) {
        try {
            return pedidoDAO.get(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Pedido> listarTodos() {
        try {
            return pedidoDAO.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Pedido crearPedidoDesdeCarrito(Cliente cliente, String direccionDestino) {
        try {
            List<DetalleProducto> productosCarrito = detalleProductoDAO.getByCarrito(cliente.getId());
            List<DetalleReceta> recetasCarrito = detalleRecetaDAO.getByCarrito(cliente.getId());

            if (productosCarrito.isEmpty() && recetasCarrito.isEmpty()) {
                throw new IllegalStateException("El carrito está vacío");
            }

            Pedido pedido = new Pedido();
            pedido.setCliente(cliente);
            pedido.setFechaPedido(new Date());
            pedido.setDireccionDestino(direccionDestino);
            pedido.setEstado(EstadoPedido.PENDIENTE);

            double precioTotal = 0.0;
            double totalImpuestos = 0.0;

            for (DetalleProducto dp : productosCarrito) {
                Producto prod = dp.getProducto();
                int cantidad = dp.getCantidad();
                double precioBase = prod.getPrecio();
                double descuento = prod.getDescuento();
                double precioConDescuento = precioBase - (precioBase * descuento / 100);
                double subtotal = precioConDescuento * cantidad;
                double impuestos = CalculadoraImpuestos.calcularTotalImpuestos(prod, cantidad, precioConDescuento);
                dp.setMontoTotal(subtotal + impuestos);
                dp.setDescuentoTotal(precioBase * cantidad - precioConDescuento * cantidad);
                dp.setTotalImpuestos(impuestos);

                precioTotal += dp.getMontoTotal();
                totalImpuestos += impuestos;
            }

            for (DetalleReceta dr : recetasCarrito) {
                double subtotalReceta = 0.0;
                double impuestosReceta = 0.0;
                for (DetalleElementoReceta der : dr.getElementosDesglosados()) {
                    Producto prod = der.getElementoBase().getProducto();
                    int cant = der.getCantidadEspecifica();
                    double precioBase = prod.getPrecio();
                    double descuento = prod.getDescuento();
                    double precioConDescuento = precioBase - (precioBase * descuento / 100);
                    subtotalReceta += precioConDescuento * cant;
                    impuestosReceta += CalculadoraImpuestos.calcularTotalImpuestos(prod, cant, precioConDescuento);
                }
                dr.setMontoTotal(subtotalReceta + impuestosReceta);
                dr.setDescuentoTotal(0.0); // sin descuento sobre receta
                dr.setTotalImpuestos(impuestosReceta);
                detalleRecetaService.recalcularMonto(dr);
                precioTotal += dr.getMontoTotal();
                totalImpuestos += impuestosReceta;
            }

            pedido.setPrecioTotal(precioTotal);
            pedido.setTotalImpuestos(totalImpuestos);
            pedido.setPrecioDelivery(0.0);
            pedido = pedidoDAO.save(pedido);

            for (DetalleProducto dp : productosCarrito) {
                detalleProductoDAO.update(dp, pedido.getIdPedido(), null);
            }
            for (DetalleReceta dr : recetasCarrito) {
                detalleRecetaDAO.update(dr, pedido.getIdPedido(), null);
            }

            cliente.setPedidoActivo(pedido);
            return pedido;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear pedido", e);
        }
    }

    @Override
    public Pedido actualizar(Pedido pedido) {
        try {
            return pedidoDAO.update(pedido);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelarPedido(Pedido pedido) {
        pedido.setEstado(EstadoPedido.CANCELADO);
        actualizar(pedido);
    }

    @Override
    public void cambiarEstado(Pedido pedido, String nuevoEstado) {
        try {
            pedido.setEstado(EstadoPedido.valueOf(nuevoEstado));
            pedidoDAO.update(pedido);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado inválido: " + nuevoEstado);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Pedido> obtenerHistorialCliente(Cliente cliente) {
        try {
            return pedidoDAO.getAll().stream()
                    .filter(p -> p.getCliente().getId() == cliente.getId())
                    .toList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}