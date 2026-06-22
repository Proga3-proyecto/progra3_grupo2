package com.licoreria.BusinessLayer.catalogo;

import com.licoreria.DBmanager.DBManager;
import com.licoreria.dao.catalogo.CategoriaDAO;
import com.licoreria.dao.catalogo.CategoriaDAOImpl;
import com.licoreria.dominio.catalogo.Categoria;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CategoriaBLImpl implements CategoriaBL {
    private final CategoriaDAO categoriaDAO;

    public CategoriaBLImpl() {
        this.categoriaDAO = new CategoriaDAOImpl();
    }

    @Override
    public List<Categoria> getAll() {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return categoriaDAO.getAll(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Categoria get(int id) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return categoriaDAO.get(con, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Categoria save(Categoria categoria) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return categoriaDAO.save(con, categoria);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Categoria update(Categoria categoria) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return categoriaDAO.update(con, categoria);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String nombre) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            Categoria categoria = categoriaDAO.get(con, nombre);
            if (categoria != null) {
                categoriaDAO.remove(con, categoria);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}