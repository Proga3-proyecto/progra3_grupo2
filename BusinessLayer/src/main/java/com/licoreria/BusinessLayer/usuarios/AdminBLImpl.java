package com.licoreria.BusinessLayer.usuarios;

import com.licoreria.DBmanager.DBManager;
import com.licoreria.DBmanager.TransactionContext;
import com.licoreria.dao.usuarios.AdminDAO;
import com.licoreria.dao.usuarios.AdminDAOImpl;
import com.licoreria.dominio.usuarios.Admin;
import java.sql.Connection;
import java.util.List;

public class AdminBLImpl implements AdminBL {
    private final AdminDAO adminDAO;

    public AdminBLImpl() {
        this.adminDAO = new AdminDAOImpl();
    }

    @Override
    public List<Admin> getAll() {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return adminDAO.getAll(con);
        } catch (Exception e) {
            throw new RuntimeException("Error al listar administradores", e);
        }
    }

    @Override
    public Admin get(int id) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return adminDAO.get(con, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener administrador", e);
        }
    }

    @Override
    public Admin save(Admin admin) {
        try {
            Connection con = TransactionContext.getConnection();
            try {
                adminDAO.save(con, admin);
                TransactionContext.commit();
                return admin;
            } catch (Exception e) {
                TransactionContext.rollback();
                throw e;
            } finally {
                TransactionContext.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar administrador", e);
        }
    }

    @Override
    public Admin update(Admin admin) {
        try {
            Connection con = TransactionContext.getConnection();
            try {
                adminDAO.update(con, admin);
                TransactionContext.commit();
                return admin;
            } catch (Exception e) {
                TransactionContext.rollback();
                throw e;
            } finally {
                TransactionContext.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar administrador", e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            Connection con = TransactionContext.getConnection();
            try {
                Admin admin = adminDAO.get(con, id);
                if (admin != null) {
                    adminDAO.remove(con, admin);
                }
                TransactionContext.commit();
            } catch (Exception e) {
                TransactionContext.rollback();
                throw e;
            } finally {
                TransactionContext.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar administrador", e);
        }
    }
    @Override
    public Admin validarCredenciales(String usuario, String password){
        try (Connection con = DBManager.getInstance().getConnection()) {
            return adminDAO.getPorCorreo(con, usuario, password);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener administrador", e);
        }
    }
}