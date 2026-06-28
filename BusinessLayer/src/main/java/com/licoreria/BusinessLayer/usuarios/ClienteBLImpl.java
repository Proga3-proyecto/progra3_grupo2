package com.licoreria.BusinessLayer.usuarios;

import com.licoreria.DBmanager.DBManager;
import com.licoreria.DBmanager.TransactionContext;
import com.licoreria.dao.carrito.PedidoDAO;
import com.licoreria.dao.carrito.PedidoDAOImpl;
import com.licoreria.dao.catalogo.*;
import com.licoreria.dao.usuarios.ClienteDAO;
import com.licoreria.dao.usuarios.ClienteDAOImpl;
import com.licoreria.dominio.carrito.DetalleProducto;
import com.licoreria.dominio.carrito.DetalleReceta;
import com.licoreria.dominio.carrito.Pedido;
import com.licoreria.dominio.catalogo.ElementoReceta;
import com.licoreria.dominio.catalogo.Imagen;
import com.licoreria.dominio.catalogo.Producto;
import com.licoreria.dominio.catalogo.Receta;
import com.licoreria.dominio.usuarios.Cliente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class ClienteBLImpl implements ClienteBL {
    private final ClienteDAO clienteDAO;
    private final ProductoDAO productoDAO;
    private final RecetaDAOImpl recetaDAO;
    private final ImagenDAO imagenDAO;
    private final PedidoDAO pedidoDAO;

    public ClienteBLImpl() {
        this.clienteDAO = new ClienteDAOImpl();
        productoDAO = new ProductoDAOImpl();
        recetaDAO = new RecetaDAOImpl();
        pedidoDAO = new PedidoDAOImpl();
        imagenDAO = new ImagenDAOImpl();
    }

    @Override
    public List<Cliente> getAll() {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return clienteDAO.getAll(con);
        } catch (Exception e) {
            throw new RuntimeException("Error al listar clientes", e);
        }
    }

    @Override
    public Cliente get(int id) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return clienteDAO.get(con, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener cliente", e);
        }
    }

    @Override
    public Cliente save(Cliente cliente) {
        try {
            Connection con = TransactionContext.getConnection();
            try {
                clienteDAO.save(con, cliente);
                TransactionContext.commit();
                return cliente;
            } catch (Exception e) {
                TransactionContext.rollback();
                throw e;
            } finally {
                TransactionContext.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar cliente", e);
        }
    }

    @Override
    public Cliente update(Cliente cliente) {
        try {
            Connection con = TransactionContext.getConnection();
            try {
                clienteDAO.update(con, cliente);
                TransactionContext.commit();
                return cliente;
            } catch (Exception e) {
                TransactionContext.rollback();
                throw e;
            } finally {
                TransactionContext.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar cliente", e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            Connection con = TransactionContext.getConnection();
            try {
                Cliente cliente = clienteDAO.get(con, id);
                if (cliente != null) {
                    clienteDAO.remove(con, cliente);
                }
                TransactionContext.commit();
            } catch (Exception e) {
                TransactionContext.rollback();
                throw e;
            } finally {
                TransactionContext.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar cliente", e);
        }
    }

    @Override
    public List<DetalleProducto> getProductosEnCarrito(int idCliente) {
        try (Connection con = DBManager.getInstance().getConnection()) {

            List<DetalleProducto> detalles = clienteDAO.getDetalleProductos(con, idCliente);
            List<Integer> productosids = detalles.stream().map(p -> p.getProducto().getId()).toList();
            Map<Integer, Producto> productos = productoDAO.getMapByIds(con, productosids);
            Map<Integer, List<Imagen>> imagenes = imagenDAO.getAllByProducts(con, productosids);

            for (DetalleProducto detalle : detalles) {
                Producto producto = productos.get(detalle.getProducto().getId());
                producto.setImagenes(imagenes.get(producto.getId()));
                detalle.setProducto(producto);
            }

            return detalles;
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener productos del carrito", e);
        }
    }

    @Override
    public List<DetalleReceta> getRecetasEnCarrito(int idCliente) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            List<DetalleReceta> detalles = clienteDAO.getDetalleReceta(con, idCliente);
            List<Integer> recetaIds = new ArrayList<>(detalles.stream().map(p -> p.getReceta().getId()).toList());
            Map<Integer, Receta> recetas = recetaDAO.getMapByIds(con, recetaIds);
            Map<Integer, List<Imagen>> imagenesRecetas = imagenDAO.getAllByRecetas(con, recetaIds);

            for (DetalleReceta detalle : detalles) {
                detalle.setReceta(recetas.get(detalle.getReceta().getId()));
                detalle.getReceta().setImagenes(imagenesRecetas.get(detalle.getReceta().getId()));
            }

//            List<Integer> productosIds = detalles.stream()
//                    .flatMap(p -> p.getReceta().getElementos().stream())
//                    .map(elemento -> elemento.getProducto().getId())
//                    .toList();
//
//            Map<Integer, Producto> products = productoDAO.getMapByIds(con, productosIds);
//
//            Map<Integer, List<Imagen>> imagenesProductos = imagenDAO.getAllByProducts(con, productosIds);
//
//            for (DetalleReceta detalle : detalles) {
//                for (ElementoReceta elemento : detalle.getReceta().getElementos()) {
//                    Producto producto = products.get(elemento.getProducto().getId());
//                    producto.setImagenes(imagenesProductos.get(producto.getId()));
//                    elemento.setProducto(producto);
//                }
//                Receta receta = detalle.getReceta();
//                receta.setImagenes(imagenesRecetas.get(receta.getId()));
//                detalle.setReceta(receta);
//            }

            return detalles;

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener recetas del carrito", e);
        }
    }

    @Override
    public Cliente validarCredenciales(String usuario, String password) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return clienteDAO.getPorCorreo(con, usuario, password);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener administrador", e);
        }
    }

    @Override
    public List<Pedido> getPedidos(int idCliente) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return pedidoDAO.getPedidosPorCliente(con, idCliente);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener pedidos del cliente", e);
        }
    }

    @Override
    public void agregarProductoAlCarrito(int idCliente, int idProducto, int cantidad) {
        try {
            Connection con = TransactionContext.getConnection();
            try {
                // Obtener el producto para calcular precios y descuentos
                Producto producto = productoDAO.get(con, idProducto);
                if (producto == null) {
                    throw new RuntimeException("El producto no existe");
                }

                double descuentoUnidad = producto.getDescuento();
                double precioUnidad = producto.getPrecioFinal(); // o getPrecio() dependiendo del requerimiento

                double descuentoTotal = descuentoUnidad * cantidad;
                double montoTotal = precioUnidad * cantidad;

                clienteDAO.agregarProductoAlCarrito(con, idCliente, idProducto, cantidad, descuentoTotal, montoTotal);

                TransactionContext.commit();
            } catch (Exception e) {
                TransactionContext.rollback();
                throw e;
            } finally {
                TransactionContext.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al agregar producto al carrito: " + e.getMessage(), e);
        }
    }

    @Override
    public void agregarRecetaAlCarrito(int idCliente, int idReceta, int cantidad) {
        try {
            Connection con = TransactionContext.getConnection();
            try {
                Receta receta = recetaDAO.get(con, idReceta);
                if (receta == null) {
                    throw new RuntimeException("La receta no existe");
                }

                double descuentoUnidad = receta.getDescuento();
                double precioUnidad = receta.getPrecioFinal();

                double descuentoTotal = descuentoUnidad * cantidad;
                double montoTotal = precioUnidad * cantidad;

                clienteDAO.agregarRecetaAlCarrito(con, idCliente, idReceta, cantidad, descuentoTotal, montoTotal);

                TransactionContext.commit();
            } catch (Exception e) {
                TransactionContext.rollback();
                throw e;
            } finally {
                TransactionContext.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al agregar receta al carrito: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminarProductoDelCarrito(int idCliente, int idProducto) {
        try {
            Connection con = TransactionContext.getConnection();
            try {
                clienteDAO.eliminarProductoDelCarrito(con, idCliente, idProducto);
                TransactionContext.commit();
            } catch (Exception e) {
                TransactionContext.rollback();
                throw e;
            } finally {
                TransactionContext.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar producto del carrito", e);
        }
    }

    @Override
    public void eliminarRecetaDelCarrito(int idCliente, int idReceta) {
        try {
            Connection con = TransactionContext.getConnection();
            try {
                clienteDAO.eliminarRecetaDelCarrito(con, idCliente, idReceta);
                TransactionContext.commit();
            } catch (Exception e) {
                TransactionContext.rollback();
                throw e;
            } finally {
                TransactionContext.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar receta del carrito", e);
        }
    }

    @Override
    public void actualizarCantidadProductoEnCarrito(int idCliente, int idProducto, int cantidad) {
        try {
            Connection con = TransactionContext.getConnection();
            try {
                Producto producto = productoDAO.get(con, idProducto);
                if (producto == null) {
                    throw new RuntimeException("El producto no existe");
                }

                double descuentoUnidad = producto.getDescuento();
                double precioUnidad = producto.getPrecioFinal();

                double descuentoTotal = descuentoUnidad * cantidad;
                double montoTotal = precioUnidad * cantidad;

                clienteDAO.actualizarCantidadProductoEnCarrito(con, idCliente, idProducto, cantidad, descuentoTotal, montoTotal);

                TransactionContext.commit();
            } catch (Exception e) {
                TransactionContext.rollback();
                throw e;
            } finally {
                TransactionContext.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar cantidad del producto: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizarCantidadRecetaEnCarrito(int idCliente, int idReceta, int cantidad) {
        try {
            Connection con = TransactionContext.getConnection();
            try {
                Receta receta = recetaDAO.get(con, idReceta);
                if (receta == null) {
                    throw new RuntimeException("La receta no existe");
                }

                double descuentoUnidad = receta.getDescuento();
                double precioUnidad = receta.getPrecioFinal();

                double descuentoTotal = descuentoUnidad * cantidad;
                double montoTotal = precioUnidad * cantidad;

                clienteDAO.actualizarCantidadRecetaEnCarrito(con, idCliente, idReceta, cantidad, descuentoTotal, montoTotal);

                TransactionContext.commit();
            } catch (Exception e) {
                TransactionContext.rollback();
                throw e;
            } finally {
                TransactionContext.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar cantidad de la receta: " + e.getMessage(), e);
        }
    }

    @Override
    public void limpiarCarrito(int idCliente) {
        try {
            Connection con = TransactionContext.getConnection();
            try {
                this.clienteDAO.limpiarPedido(con, idCliente);
                TransactionContext.commit();
            } catch (Exception e) {
                TransactionContext.rollback();
                throw e;
            } finally {
                TransactionContext.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar cantidad de la receta: " + e.getMessage(), e);
        }
    }
}