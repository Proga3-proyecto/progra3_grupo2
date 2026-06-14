package com.licoreria.BusinessLayer.RecetaService;

import com.licoreria.dao.RecetaDAO;
import com.licoreria.dao.impl.RecetaDAOImpl;
import com.licoreria.dominio.productos.Receta;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class RecetaServiceImpl  implements IRecetaService {
    private final RecetaDAO recetaDAO;

    public RecetaServiceImpl() {
        this.recetaDAO = new RecetaDAOImpl();
    }

    @Override
    public Receta obtenerPorId(Long id) {
        try {
            return recetaDAO.get(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener receta con id " + id, e);
        }
    }

    @Override
    public List<Receta> listarTodas() {
        try {
            return recetaDAO.getAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar recetas", e);
        }
    }

    @Override
    public Receta crear(Receta receta) {
        try {
            return recetaDAO.save(receta);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear receta", e);
        }
    }

    @Override
    public Receta actualizar(Receta receta) {
        try {
            return recetaDAO.update(receta);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar receta", e);
        }
    }

    @Override
    public void eliminar(Receta receta) {
        try {
            recetaDAO.remove(receta);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar receta", e);
        }
    }

    @Override
    public List<Receta> buscarPorNombre(String nombre) {
        return listarTodas().stream()
                .filter(r -> r.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }
}