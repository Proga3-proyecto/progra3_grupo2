package com.licoreria.BusinessLayer.carrito;

import com.licoreria.DBmanager.DBManager;
import com.licoreria.DBmanager.TransactionContext;
import com.licoreria.dao.carrito.PedidoDAO;
import com.licoreria.dao.carrito.PedidoDAOImpl;
import com.licoreria.dominio.carrito.EstadoPedido;
import com.licoreria.dominio.carrito.Pedido;

import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoBLImpl implements PedidoBL {

    private final PedidoDAO pedidoDAO;

    public PedidoBLImpl() {
        this.pedidoDAO = new PedidoDAOImpl();
    }

    @Override
    public List<Pedido> getAll() {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return pedidoDAO.getAll(con);
        } catch (Exception e) {
            throw new RuntimeException("Error al listar los pedidos", e);
        }
    }

    @Override
    public Pedido get(int id) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return pedidoDAO.get(con, id);
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
            return todos.stream()
                    .filter(p -> p.getCliente() != null && p.getCliente().getIdUsuario() == idCliente)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los pedidos del cliente " + idCliente, e);
        }
    }

    @Override
    public List<Pedido> getPedidosPorEstado(EstadoPedido estado) {
        // Al igual que el método anterior, se recomienda crear un método específico en el DAO para escalabilidad.
        try (Connection con = DBManager.getInstance().getConnection()) {
            List<Pedido> todos = pedidoDAO.getAll(con);
            return todos.stream()
                    .filter(p -> p.getEstado() == estado)
                    .collect(Collectors.toList());
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
}