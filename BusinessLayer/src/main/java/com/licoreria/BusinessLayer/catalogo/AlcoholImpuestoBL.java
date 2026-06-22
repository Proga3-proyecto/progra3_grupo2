package com.licoreria.BusinessLayer.catalogo;

import com.licoreria.dominio.catalogo.AlcoholImpuesto;
import java.util.List;

public interface AlcoholImpuestoBL {
    List<AlcoholImpuesto> getAll();
    AlcoholImpuesto get(int id);
    AlcoholImpuesto save(AlcoholImpuesto ai);
    AlcoholImpuesto update(AlcoholImpuesto ai);
    void delete(int id);
}