package com.licoreria.app.dao.clienteDAO;

import com.licoreria.app.modelo.usuarios.Cliente;

import java.util.List;

public interface ClienteDAO {
    Cliente get(long id);
    List<Cliente> getAll();
    void save(Cliente cliente);
    void update(Cliente cliente);
    void delete(Cliente cliente);
}
