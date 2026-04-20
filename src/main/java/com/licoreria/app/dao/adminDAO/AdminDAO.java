package com.licoreria.app.dao.adminDAO;

import com.licoreria.app.modelo.usuarios.Admin;
import java.util.List;

public interface AdminDAO {
    Admin get(long id);
    List<Admin> getAll();
    void save(Admin admin);
    void update(Admin admin);
    void delete(Admin admin);
}