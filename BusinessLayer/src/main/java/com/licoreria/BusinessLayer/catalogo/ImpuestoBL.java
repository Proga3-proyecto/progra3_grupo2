package com.licoreria.BusinessLayer.catalogo;

import com.licoreria.dominio.catalogo.Impuesto;
import java.util.List;

public interface ImpuestoBL {
    List<Impuesto> getAll();
    Impuesto save(Impuesto impuesto);
    Impuesto update(Impuesto impuesto);
    void delete(int id);
}