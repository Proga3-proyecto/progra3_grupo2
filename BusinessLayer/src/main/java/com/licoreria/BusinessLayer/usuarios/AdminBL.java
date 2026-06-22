package com.licoreria.BusinessLayer.usuarios;

import com.licoreria.dominio.usuarios.Admin;
import java.util.List;

public interface AdminBL {
    List<Admin> getAll();
    Admin get(int id);
    Admin save(Admin admin);
    Admin update(Admin admin);
    void delete(int id);
}