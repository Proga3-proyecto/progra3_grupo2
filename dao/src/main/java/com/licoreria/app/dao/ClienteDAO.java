package com.licoreria.app.dao;


import com.licoreria.app.dominio.usuarios.Admin;
import com.licoreria.app.dominio.usuarios.Cliente;

import java.util.List;
public interface ClienteDAO {
    Cliente get(long id);
    List<Cliente> getAll();
    void save(Cliente cliente);
    void update(Cliente cliente);
    void delete(Cliente cliente);

    interface AdminDAO {
        Admin get(long id);
        List<Admin> getAll();
        void save(Admin admin);
        void update(Admin admin);
        void delete(Admin admin);
    }
}
