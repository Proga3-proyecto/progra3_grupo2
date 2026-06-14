package com.licoreria.BusinessLayer.ImpuestoService;

import com.licoreria.dao.ImpuestoDAO;
import com.licoreria.dao.impl.ImpuestoDAOImpl;
import com.licoreria.dominio.productos.Impuesto;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ImpuestoServiceImpl  implements IImpuestoService {
    private final ImpuestoDAO impuestoDAO;

    public ImpuestoServiceImpl() {
        this.impuestoDAO = new ImpuestoDAOImpl();
    }

    @Override
    public Impuesto obtenerPorId(Long id) {
        try {
            return impuestoDAO.get(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener impuesto con id " + id, e);
        }
    }

    @Override
    public List<Impuesto> listarTodos() {
        try {
            return impuestoDAO.getAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar impuestos", e);
        }
    }

    @Override
    public List<Impuesto> listarActivos() {
        return listarTodos().stream()
                .filter(Impuesto::getActivo)
                .collect(Collectors.toList());
    }

    @Override
    public Impuesto crear(Impuesto impuesto) {
        try {
            return impuestoDAO.save(impuesto);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear impuesto", e);
        }
    }

    @Override
    public Impuesto actualizar(Impuesto impuesto) {
        try {
            return impuestoDAO.update(impuesto);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar impuesto", e);
        }
    }

    @Override
    public void eliminar(Impuesto impuesto) {
        try {
            impuestoDAO.remove(impuesto);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar impuesto", e);
        }
    }

    @Override
    public void activarDesactivar(Long id, boolean activo) {
        Impuesto imp = obtenerPorId(id);
        imp.setActivo(activo);
        actualizar(imp);
    }
}