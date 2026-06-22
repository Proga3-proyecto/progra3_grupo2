package com.licoreria.BusinessLayer.usuarios;

import com.licoreria.DBmanager.DBManager;
import com.licoreria.DBmanager.TransactionContext;
import com.licoreria.dao.catalogo.ProductoDAO;
import com.licoreria.dao.catalogo.ProductoDAOImpl;
import com.licoreria.dao.catalogo.RecetaDAOImpl;
import com.licoreria.dao.usuarios.ClienteDAO;
import com.licoreria.dao.usuarios.ClienteDAOImpl;
import com.licoreria.dominio.catalogo.Producto;
import com.licoreria.dominio.catalogo.Receta;
import com.licoreria.dominio.usuarios.Cliente;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ClienteBLImpl implements ClienteBL {
    private final ClienteDAO clienteDAO;
    private final ProductoDAO productoDAO;
    private final RecetaDAOImpl recetaDAO;
    public ClienteBLImpl() {
        this.clienteDAO = new ClienteDAOImpl();
        productoDAO =  new ProductoDAOImpl();
        recetaDAO = new RecetaDAOImpl();
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
    public List<Producto> getProductosEnCarrito(int idCliente) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return productoDAO.getProductosPorCliente(con, idCliente);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener productos del carrito", e);
        }
    }

    @Override
    public List<Receta> getRecetasEnCarrito(int idCliente) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return recetaDAO.getRecetasPorCliente(con, idCliente);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener recetas del carrito", e);
        }
    }
}