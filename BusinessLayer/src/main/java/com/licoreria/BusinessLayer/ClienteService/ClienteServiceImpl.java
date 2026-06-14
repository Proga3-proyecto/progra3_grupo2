package com.licoreria.BusinessLayer.ClienteService;

import com.licoreria.dao.ClienteDAO;
import com.licoreria.dao.DetalleProductoDAO;
import com.licoreria.dao.DetalleRecetaDAO;
import com.licoreria.dao.impl.ClienteDAOImpl;
import com.licoreria.dao.impl.DetalleProductoDAOImpl;
import com.licoreria.dao.impl.DetalleRecetaDAOImpl;
import com.licoreria.dominio.pedidos.DetalleProducto;
import com.licoreria.dominio.pedidos.DetalleReceta;
import com.licoreria.dominio.usuarios.Cliente;

import java.sql.SQLException;
import java.util.List;

public class ClienteServiceImpl implements IClienteService {
    private final ClienteDAO clienteDAO;
    private final DetalleProductoDAO detalleProductoDAO;
    private final DetalleRecetaDAO detalleRecetaDAO;

    public ClienteServiceImpl() {
        this.clienteDAO = new ClienteDAOImpl();
        this.detalleProductoDAO = new DetalleProductoDAOImpl();
        this.detalleRecetaDAO = new DetalleRecetaDAOImpl();
    }

    @Override
    public Cliente obtenerPorId(Long id) {
        try {
            return clienteDAO.get(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Cliente> listarTodos() {
        try {
            return clienteDAO.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Cliente crear(Cliente cliente) {
        try {
            return clienteDAO.save(cliente);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Cliente actualizar(Cliente cliente) {
        try {
            return clienteDAO.update(cliente);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Cliente cliente) {
        try {
            clienteDAO.remove(cliente);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DetalleProducto> obtenerCarritoProductos(Cliente cliente) {
        try {
            return detalleProductoDAO.getByCarrito(cliente.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener carrito de productos", e);
        }
    }

    @Override
    public void agregarProductoAlCarrito(Cliente cliente, DetalleProducto detalle) {
        try {
            detalleProductoDAO.save(detalle, null, cliente.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Error al agregar producto al carrito", e);
        }
    }

    @Override
    public void eliminarProductoDelCarrito(Cliente cliente, Long idDetalleProducto) {
        try {
            DetalleProducto detalle = detalleProductoDAO.get(idDetalleProducto);
            if (detalle != null) {
                detalleProductoDAO.remove(detalle);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar producto del carrito", e);
        }
    }

    @Override
    public void limpiarCarritoProductos(Cliente cliente) {
        List<DetalleProducto> carrito = obtenerCarritoProductos(cliente);
        for (DetalleProducto dp : carrito) {
            try {
                detalleProductoDAO.remove(dp);
            } catch (SQLException e) {
                throw new RuntimeException("Error al limpiar carrito de productos", e);
            }
        }
    }

    @Override
    public List<DetalleReceta> obtenerCarritoRecetas(Cliente cliente) {
        try {
            return detalleRecetaDAO.getByCarrito(cliente.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener carrito de recetas", e);
        }
    }

    @Override
    public void agregarRecetaAlCarrito(Cliente cliente, DetalleReceta detalle) {
        try {
            detalleRecetaDAO.save(detalle, null, cliente.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Error al agregar receta al carrito", e);
        }
    }

    @Override
    public void eliminarRecetaDelCarrito(Cliente cliente, Long idDetalleReceta) {
        try {
            DetalleReceta detalle = detalleRecetaDAO.get(idDetalleReceta);
            if (detalle != null) {
                detalleRecetaDAO.remove(detalle);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar receta del carrito", e);
        }
    }

    @Override
    public void limpiarCarritoRecetas(Cliente cliente) {
        List<DetalleReceta> carrito = obtenerCarritoRecetas(cliente);
        for (DetalleReceta dr : carrito) {
            try {
                detalleRecetaDAO.remove(dr);
            } catch (SQLException e) {
                throw new RuntimeException("Error al limpiar carrito de recetas", e);
            }
        }
    }
}