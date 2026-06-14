package com.licoreria.BusinessLayer.AdminService;

import com.licoreria.dao.AdminDAO;
import com.licoreria.dao.impl.AdminDAOImpl;
import com.licoreria.dominio.usuarios.Admin;

import java.sql.SQLException;
import java.util.List;

public class AdminServiceImpl implements IAdminService {
    private final AdminDAO adminDAO;

    public AdminServiceImpl() {
        this.adminDAO = new AdminDAOImpl();
    }

    @Override
    public Admin obtenerPorId(Long id) {
        try {
            return adminDAO.get(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener admin", e);
        }
    }

    @Override
    public List<Admin> listarTodos() {
        try {
            return adminDAO.getAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar admins", e);
        }
    }

    @Override
    public Admin crear(Admin admin) {
        try {
            return adminDAO.save(admin);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear admin", e);
        }
    }

    @Override
    public Admin actualizar(Admin admin) {
        try {
            return adminDAO.update(admin);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar admin", e);
        }
    }

    @Override
    public void eliminar(Admin admin) {
        try {
            adminDAO.remove(admin);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar admin", e);
        }
    }
}