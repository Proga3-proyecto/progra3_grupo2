package com.licoreria.BusinessLayer.catalogo;

import com.licoreria.dominio.catalogo.Categoria;
import java.util.List;

public interface CategoriaBL {
    List<Categoria> getAll();
    Categoria get(int id);
    Categoria save(Categoria categoria);
    Categoria update(Categoria categoria);
    void delete(String id);
}