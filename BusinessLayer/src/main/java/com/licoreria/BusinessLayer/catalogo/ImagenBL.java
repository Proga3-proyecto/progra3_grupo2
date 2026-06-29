package com.licoreria.BusinessLayer.catalogo;

import com.licoreria.dominio.catalogo.Imagen;
import java.util.List;

public interface ImagenBL {
    List<Imagen> getAll();
    Imagen get(int id);
    void delete(Imagen imagen);
    void checkAndRemoveUnusedImage(int idImagen);
    void limpiarImagenesHuerfanasAsync();
}