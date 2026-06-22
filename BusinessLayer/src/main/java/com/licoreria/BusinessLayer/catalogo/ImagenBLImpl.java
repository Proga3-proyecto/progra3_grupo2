package com.licoreria.BusinessLayer.catalogo;

import com.licoreria.DBmanager.DBManager;
import com.licoreria.dao.catalogo.ImagenDAO;
import com.licoreria.dao.catalogo.ImagenDAOImpl;
import com.licoreria.dominio.catalogo.Imagen;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ImagenBLImpl implements ImagenBL {
    private ImagenDAO imagenDAO;

    public ImagenBLImpl() {
        this.imagenDAO = new ImagenDAOImpl();
    }

    @Override
    public List<Imagen> getAll() {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return imagenDAO.getAll(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Imagen get(int id) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return imagenDAO.get(con, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Imagen imagen) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            imagenDAO.remove(con, imagen);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}