package com.licoreria.BusinessLayer.catalogo;

import com.licoreria.dominio.catalogo.Marca;
import java.util.List;

public interface MarcaBL {
    List<Marca> getAll();
    Marca save(Marca marca);
    void delete(String nombre);
}