package com.licoreria.BusinessLayer.ImpuestoService;

import com.licoreria.dominio.productos.Impuesto;

import java.util.List;

public interface IImpuestoService  {
    Impuesto obtenerPorId(Long id);
    List<Impuesto> listarTodos();
    List<Impuesto> listarActivos();
    Impuesto crear(Impuesto impuesto);
    Impuesto actualizar(Impuesto impuesto);
    void eliminar(Impuesto impuesto);
    void activarDesactivar(Long id, boolean activo);
}