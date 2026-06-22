package com.licoreria.BusinessLayer.catalogo;

import com.licoreria.DBmanager.DBManager;
import com.licoreria.dao.catalogo.ImpuestoDAO;
import com.licoreria.dao.catalogo.ImpuestoDAOImpl;
import com.licoreria.dominio.catalogo.Impuesto;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ImpuestoBLImpl implements ImpuestoBL {
    private final ImpuestoDAO dao;

    public ImpuestoBLImpl() {
        this.dao = new ImpuestoDAOImpl();
    }

    @Override
    public List<Impuesto> getAll() {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return dao.getAll(con);
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar impuestos: " + e.getMessage());
        }
    }

    @Override
    public Impuesto save(Impuesto impuesto) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return dao.save(con, impuesto);
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar impuesto: " + e.getMessage());
        }
    }

    @Override
    public Impuesto update(Impuesto impuesto) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return dao.update(con, impuesto);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar impuesto: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            Impuesto impuesto = dao.get(con, id);
            if (impuesto != null) {
                dao.remove(con, impuesto);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar impuesto: " + e.getMessage());
        }
    }
}