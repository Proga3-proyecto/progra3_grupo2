package com.licoreria.app.dao;


import com.licoreria.app.dominio.productos.Receta;

import java.util.List;

public interface RecetaDAO {
    Receta get(long id);

    List<Receta> getAll();

    void save(Receta receta);

    void update(Receta receta);

    void delete(Receta receta);
}