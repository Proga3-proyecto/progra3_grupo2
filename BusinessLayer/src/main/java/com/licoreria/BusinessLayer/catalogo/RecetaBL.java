package com.licoreria.BusinessLayer.catalogo;

import com.licoreria.dominio.catalogo.Categoria;
import com.licoreria.dominio.catalogo.Imagen;
import com.licoreria.dominio.catalogo.Receta;

import java.util.List;

public interface RecetaBL {
    List<Receta> getAll();

    Receta get(int id);

    Receta save(Receta receta);

    Receta update(Receta receta);

    void delete(int id);

    Imagen agregarImagen(Receta receta, String url);

    void agregarImagenPrincipal(Receta receta, int idImagen);

    void removerImagen(int idReceta, int idImagen);

    void agregarCategoria(Receta receta, Categoria categoria);

    void eliminarCategoria(Receta receta, String nombreCategoria);
}