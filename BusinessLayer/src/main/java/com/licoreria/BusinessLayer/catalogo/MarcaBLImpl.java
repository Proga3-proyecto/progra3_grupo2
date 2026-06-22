package com.licoreria.BusinessLayer.catalogo;

import com.licoreria.DBmanager.DBManager;
import com.licoreria.dao.catalogo.MarcaDAO;
import com.licoreria.dao.catalogo.MarcaDAOImpl;
import com.licoreria.dominio.catalogo.Marca;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MarcaBLImpl implements MarcaBL {
    private final MarcaDAO marcaDAO;

    public MarcaBLImpl() {
        this.marcaDAO = new MarcaDAOImpl();
    }

    @Override
    public List<Marca> getAll() {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return marcaDAO.getAll(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Marca save(Marca marca) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return marcaDAO.save(con, marca);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String nombre) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            Marca marca = marcaDAO.get(con, nombre);
            if (marca != null) {
                marcaDAO.remove(con, marca);
            } else {
                throw new RuntimeException("No se encontró la marca: " + nombre);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}