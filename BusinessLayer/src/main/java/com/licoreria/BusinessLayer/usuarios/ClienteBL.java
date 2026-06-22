package com.licoreria.BusinessLayer.usuarios;

import com.licoreria.dominio.catalogo.Producto;
import com.licoreria.dominio.catalogo.Receta;
import com.licoreria.dominio.usuarios.Cliente;

import java.util.List;

public interface ClienteBL {
    List<Cliente> getAll();

    Cliente get(int id);

    Cliente save(Cliente cliente);

    Cliente update(Cliente cliente);

    void delete(int id);

    List<Producto> getProductosEnCarrito(int idCliente);
    List<Receta> getRecetasEnCarrito(int idCliente);
}