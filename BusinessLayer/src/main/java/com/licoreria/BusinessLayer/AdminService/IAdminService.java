package com.licoreria.BusinessLayer.AdminService;

import com.licoreria.dominio.usuarios.Admin;

import java.util.List;

public interface IAdminService {
    Admin obtenerPorId(Long id);
    List<Admin> listarTodos();
    Admin crear(Admin admin);
    Admin actualizar(Admin admin);
    void eliminar(Admin admin);
}