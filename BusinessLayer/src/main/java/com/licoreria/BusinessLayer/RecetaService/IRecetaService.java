package com.licoreria.BusinessLayer.RecetaService;

import com.licoreria.dominio.productos.Receta;

import java.util.List;

public interface IRecetaService {
    Receta obtenerPorId(Long id);
    List<Receta> listarTodas();
    Receta crear(Receta receta);
    Receta actualizar(Receta receta);
    void eliminar(Receta receta);
    List<Receta> buscarPorNombre(String nombre);
}