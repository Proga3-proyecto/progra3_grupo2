package com.licoreria.BusinessLayer.carrito;

import com.licoreria.DBmanager.DBManager;
import com.licoreria.DBmanager.TransactionContext;
import com.licoreria.dao.carrito.PedidoDAO;
import com.licoreria.dao.carrito.PedidoDAOImpl;
import com.licoreria.dao.carrito.ProductoSnapshotDAO;
import com.licoreria.dao.carrito.ProductoSnapshotDAOImpl;
import com.licoreria.dao.carrito.RecetaSnapshotDAO;
import com.licoreria.dao.carrito.RecetaSnapshotDAOImpl;
import com.licoreria.dao.usuarios.ClienteDAO;
import com.licoreria.dao.usuarios.ClienteDAOImpl;
import com.licoreria.dominio.Snapshots.ProductoSnapshot;
import com.licoreria.dominio.Snapshots.RecetaSnapshot;
import com.licoreria.dominio.carrito.EstadoPedido;
import com.licoreria.dominio.carrito.Pedido;
import com.licoreria.dominio.usuarios.Cliente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PedidoBLImpl implements PedidoBL {

    private final PedidoDAO pedidoDAO;
    private final ClienteDAO clienteDAO;
    private final ProductoSnapshotDAO productoSnapshotDAO;
    private final RecetaSnapshotDAO recetaSnapshotDAO;

    public PedidoBLImpl() {
        this.pedidoDAO = new PedidoDAOImpl();
        this.clienteDAO = new ClienteDAOImpl();
        this.productoSnapshotDAO = new ProductoSnapshotDAOImpl();
        this.recetaSnapshotDAO = new RecetaSnapshotDAOImpl();
    }

    @Override
    public List<Pedido> getAll() {
        try (Connection con = DBManager.getInstance().getConnection()) {
            List<Pedido> pedidos = pedidoDAO.getAll(con);
            cargarDatosRelacionalesMasivo(con, pedidos);
            return pedidos;
        } catch (Exception e) {
            throw new RuntimeException("Error al listar los pedidos", e);
        }
    }

    @Override
    public Pedido get(int id) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            Pedido pedido = pedidoDAO.get(con, id);
            if (pedido != null) {
                // Al pasarlo como una lista de 1 elemento, reutilizamos el algoritmo masivo
                cargarDatosRelacionalesMasivo(con, Collections.singletonList(pedido));
            }
            return pedido;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el pedido " + id, e);
        }
    }

    @Override
    public Pedido save(Pedido pedido) {
        try {
            Connection con = TransactionContext.getConnection();
            try {
                if (pedido.getEstado() == null) {
                    pedido.setEstado(EstadoPedido.PENDIENTE);
                }

                pedidoDAO.save(con, pedido);
                TransactionContext.commit();
                return pedido;
            } catch (Exception e) {
                TransactionContext.rollback();
                throw e;
            } finally {
                TransactionContext.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar el pedido", e);
        }
    }

    @Override
    public Pedido update(Pedido pedido) {
        try {
            Connection con = TransactionContext.getConnection();
            try {
                pedidoDAO.update(con, pedido);
                TransactionContext.commit();
                return pedido;
            } catch (Exception e) {
                TransactionContext.rollback();
                throw e;
            } finally {
                TransactionContext.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el pedido", e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            Connection con = TransactionContext.getConnection();
            try {
                Pedido pedido = pedidoDAO.get(con, id);
                if (pedido != null) {
                    pedidoDAO.remove(con, pedido);
                }
                TransactionContext.commit();
            } catch (Exception e) {
                TransactionContext.rollback();
                throw e;
            } finally {
                TransactionContext.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el pedido " + id, e);
        }
    }

    @Override
    public List<Pedido> getPedidosPorCliente(int idCliente) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            List<Pedido> todos = pedidoDAO.getAll(con);
            List<Pedido> pedidosFiltrados = todos.stream()
                    .filter(p -> p.getCliente() != null && p.getCliente().getIdUsuario() == idCliente)
                    .collect(Collectors.toList());

            cargarDatosRelacionalesMasivo(con, pedidosFiltrados);
            return pedidosFiltrados;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los pedidos del cliente " + idCliente, e);
        }
    }

    @Override
    public List<Pedido> getPedidosPorEstado(EstadoPedido estado) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            List<Pedido> todos = pedidoDAO.getAll(con);
            List<Pedido> pedidosFiltrados = todos.stream()
                    .filter(p -> p.getEstado() == estado)
                    .collect(Collectors.toList());

            cargarDatosRelacionalesMasivo(con, pedidosFiltrados);
            return pedidosFiltrados;
        } catch (Exception e) {
            throw new RuntimeException("Error al filtrar pedidos por estado " + estado.name(), e);
        }
    }

    @Override
    public void actualizarEstadoPedido(int idPedido, EstadoPedido nuevoEstado) {
        try {
            Connection con = TransactionContext.getConnection();
            try {
                Pedido pedido = pedidoDAO.get(con, idPedido);
                if (pedido != null) {
                    pedido.setEstado(nuevoEstado);
                    pedidoDAO.update(con, pedido);
                } else {
                    throw new RuntimeException("El pedido con ID " + idPedido + " no existe.");
                }
                TransactionContext.commit();
            } catch (Exception e) {
                TransactionContext.rollback();
                throw e;
            } finally {
                TransactionContext.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al cambiar el estado del pedido " + idPedido, e);
        }
    }


    private void cargarDatosRelacionalesMasivo(Connection con, List<Pedido> pedidos) throws SQLException {
        if (pedidos == null || pedidos.isEmpty()) return;

        Set<Integer> idsClientes = new HashSet<>();
        Set<Integer> idsProductos = new HashSet<>();
        Set<Integer> idsRecetas = new HashSet<>();

        // 1. Recolección exhaustiva de IDs requeridos en toda la lista de pedidos
        for (Pedido pedido : pedidos) {
            if (pedido.getCliente() != null && pedido.getCliente().getIdUsuario() != null) {
                idsClientes.add(pedido.getCliente().getIdUsuario());
            }

            if (pedido.getDetallesProductos() != null) {
                for (var detalle : pedido.getDetallesProductos()) {
                    if (detalle.getProductoSnapshot() != null && detalle.getProductoSnapshot().getId() != null) {
                        idsProductos.add(detalle.getProductoSnapshot().getId());
                    }
                }
            }

            if (pedido.getDetallesRecetas() != null) {
                for (var detalle : pedido.getDetallesRecetas()) {
                    if (detalle.getRecetaSnapshot() != null && detalle.getRecetaSnapshot().getId() != null) {
                        idsRecetas.add(detalle.getRecetaSnapshot().getId());
                    }
                }
            }
        }

        Map<Integer, Cliente> mapClientes = idsClientes.isEmpty() ? Collections.emptyMap()
                : clienteDAO.getByIds(con, new ArrayList<>(idsClientes));

        Map<Integer, ProductoSnapshot> mapProductos = idsProductos.isEmpty() ? Collections.emptyMap()
                : productoSnapshotDAO.getMapByIds(con, new ArrayList<>(idsProductos));

        Map<Integer, RecetaSnapshot> mapRecetas = idsRecetas.isEmpty() ? Collections.emptyMap()
                : recetaSnapshotDAO.getMapByIds(con, new ArrayList<>(idsRecetas));

        for (Pedido pedido : pedidos) {
            if (pedido.getCliente() != null) {
                Cliente c = mapClientes.get(pedido.getCliente().getIdUsuario());
                if(c != null) pedido.setCliente(c);
            }

            if (pedido.getDetallesProductos() != null) {
                for (var detalle : pedido.getDetallesProductos()) {
                    if (detalle.getProductoSnapshot() != null) {
                        ProductoSnapshot ps = mapProductos.get(detalle.getProductoSnapshot().getId());
                        if(ps != null) detalle.setProductoSnapshot(ps);
                    }
                }
            }

            if (pedido.getDetallesRecetas() != null) {
                for (var detalle : pedido.getDetallesRecetas()) {
                    if (detalle.getRecetaSnapshot() != null) {
                        RecetaSnapshot rs = mapRecetas.get(detalle.getRecetaSnapshot().getId());
                        if (rs != null) detalle.setRecetaSnapshot(rs);
                    }
                }
            }
        }
    }
}