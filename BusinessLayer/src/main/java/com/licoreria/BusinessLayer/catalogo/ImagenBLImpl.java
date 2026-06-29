package com.licoreria.BusinessLayer.catalogo;

import com.licoreria.DBmanager.DBManager;
import com.licoreria.dao.catalogo.ImagenDAO;
import com.licoreria.dao.catalogo.ImagenDAOImpl;
import com.licoreria.dominio.catalogo.Imagen;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import com.licoreria.SupabaseDriver.SupabaseDriver;

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

    @Override
    public void checkAndRemoveUnusedImage(int idImagen) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            if (!imagenDAO.isImagenEnUso(con, idImagen)) {
                Imagen imagen = imagenDAO.get(con, idImagen);
                if (imagen != null) {
                    try {
                        SupabaseDriver supabaseDriver = new SupabaseDriver();
                        String url = imagen.getUrl();
                        String fileName = url.substring(url.lastIndexOf("/") + 1);
                        supabaseDriver.delete(fileName);
                    } catch (Exception e) {
                        System.err.println("Error deleting from Supabase: " + e.getMessage());
                    }
                    imagenDAO.remove(con, imagen);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void limpiarImagenesHuerfanasAsync() {
        //CompletableFuture.runAsync(() -> {
            try (Connection con = DBManager.getInstance().getConnection()) {
                List<Imagen> huerfanas = imagenDAO.getImagenesHuerfanas(con);
                if (huerfanas != null && !huerfanas.isEmpty()) {
                    SupabaseDriver supabaseDriver = new SupabaseDriver();
                    for (Imagen img : huerfanas) {
                        try {
                            String url = img.getUrl();
                            String fileName = url.substring(url.lastIndexOf("/") + 1);
                            supabaseDriver.delete(fileName);
                        } catch (Exception e) {
                            System.err.println("Error deleting orphaned image from Supabase: " + e.getMessage());
                        }
                        try {
                            imagenDAO.remove(con, img);
                        } catch (Exception e) {
                            System.err.println("Error deleting orphaned image from DB: " + e.getMessage());
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error running async image cleanup: " + e.getMessage());
            }
       // });
    }
}