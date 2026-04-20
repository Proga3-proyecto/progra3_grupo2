package com.licoreria.app.dao.motorizadoDAO;

import com.licoreria.app.modelo.usuarios.Motorizado;

import java.util.List;

public interface MotorizadoDAO {
    Motorizado get(long id);
    List<Motorizado> getAll();
    void save(Motorizado motorizado);
    void update(Motorizado motorizado);
    void delete(Motorizado motorizado);
}