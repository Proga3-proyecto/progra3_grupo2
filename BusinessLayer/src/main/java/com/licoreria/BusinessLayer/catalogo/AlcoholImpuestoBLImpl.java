package com.licoreria.BusinessLayer.catalogo;

import com.licoreria.DBmanager.DBManager;
import com.licoreria.dao.catalogo.AlcoholImpuestoDAO;
import com.licoreria.dao.catalogo.AlcoholImpuestoDAOImpl;
import com.licoreria.dominio.catalogo.AlcoholImpuesto;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AlcoholImpuestoBLImpl implements AlcoholImpuestoBL {
    private final AlcoholImpuestoDAO dao;

    public AlcoholImpuestoBLImpl() {
        this.dao = new AlcoholImpuestoDAOImpl();
    }

    @Override
    public List<AlcoholImpuesto> getAll() {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return dao.getAll(con);
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar impuestos: " + e.getMessage());
        }
    }

    @Override
    public AlcoholImpuesto get(int id) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return dao.get(con, id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar impuesto: " + e.getMessage());
        }
    }

    @Override
    public AlcoholImpuesto save(AlcoholImpuesto ai) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return dao.save(con, ai);
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar impuesto: " + e.getMessage());
        }
    }

    @Override
    public AlcoholImpuesto update(AlcoholImpuesto ai) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return dao.update(con, ai);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar impuesto: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            AlcoholImpuesto ai = dao.get(con, id);
            if (ai != null) dao.remove(con, ai);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar impuesto: " + e.getMessage());
        }
    }
}