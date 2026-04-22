package com.licoreria.dao.impl;

import com.licoreria.DBmanager.DBManager;
import com.licoreria.DBmanager.TransactionContext;
import com.licoreria.dao.RecetaDAO;
import com.licoreria.dominio.productos.ElementoReceta;
import com.licoreria.dominio.productos.Producto;
import com.licoreria.dominio.productos.Receta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecetaDAOImpl implements RecetaDAO {

    @Override
    public Receta get(Long id) throws SQLException {
        Receta receta = null;
        String queryReceta = "SELECT * FROM Receta WHERE id_receta = ?";

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(queryReceta)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    List<ElementoReceta> elementos = getElementosPorReceta(conn, id);
                    receta = new Receta(
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getString("image_src"),
                            elementos
                    );
                    receta.setId(rs.getLong("id_receta"));
                }
            }
        }
        return receta;
    }

    @Override
    public List<Receta> getAll() throws SQLException {
        List<Receta> recetas = new ArrayList<>();
        String query = "SELECT * FROM Receta";

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                long idReceta = rs.getLong("id_receta");
                List<ElementoReceta> elementos = getElementosPorReceta(conn, idReceta);

                Receta receta = new Receta(
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("image_src"),
                        elementos
                );
                receta.setId(idReceta);
                recetas.add(receta);
            }
        }
        return recetas;
    }

    @Override
    public Receta save(Receta receta) throws SQLException {
        String insertReceta = "INSERT INTO Receta (nombre, descripcion, image_src) VALUES (?, ?, ?)";
        String insertElemento = "INSERT INTO Elemento_Receta (id_receta, id_producto, cantidad) VALUES (?, ?, ?)";

        Connection conn = TransactionContext.getConnection();

        try {
            try (PreparedStatement psReceta = conn.prepareStatement(insertReceta, Statement.RETURN_GENERATED_KEYS)) {
                psReceta.setString(1, receta.getNombre());
                psReceta.setString(2, receta.getDescripcion());
                psReceta.setString(3, receta.getImageSRC());

                int affectedRows = psReceta.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Fallo al crear la receta, no se afectaron filas.");
                }

                try (ResultSet generatedKeys = psReceta.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long idReceta = generatedKeys.getLong(1);
                        receta.setId(idReceta);

                        if (receta.getElementos() != null && !receta.getElementos().isEmpty()) {
                            try (PreparedStatement psElemento = conn.prepareStatement(insertElemento)) {
                                for (ElementoReceta elemento : receta.getElementos()) {
                                    psElemento.setLong(1, idReceta);
                                    psElemento.setLong(2, elemento.getProducto().getId());
                                    psElemento.setDouble(3, elemento.getCantidad());
                                    psElemento.addBatch();
                                }
                                psElemento.executeBatch();
                            }
                        }
                    } else {
                        throw new SQLException("Fallo al crear la receta, no se obtuvo el ID.");
                    }
                }
            }
            TransactionContext.commit();
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
        return receta;
    }

    @Override
    public Receta update(Receta receta) throws SQLException {
        String updateReceta = "UPDATE Receta SET nombre=?, descripcion=?, image_src=? WHERE id_receta=?";
        String deleteElementos = "DELETE FROM Elemento_Receta WHERE id_receta=?";
        String insertElemento = "INSERT INTO Elemento_Receta (id_receta, id_producto, cantidad) VALUES (?, ?, ?)";

        Connection conn = TransactionContext.getConnection();

        try {
            try (PreparedStatement psReceta = conn.prepareStatement(updateReceta)) {
                psReceta.setString(1, receta.getNombre());
                psReceta.setString(2, receta.getDescripcion());
                psReceta.setString(3, receta.getImageSRC());
                psReceta.setLong(4, receta.getId());
                psReceta.executeUpdate();
            }

            try (PreparedStatement psDelete = conn.prepareStatement(deleteElementos)) {
                psDelete.setLong(1, receta.getId());
                psDelete.executeUpdate();
            }

            if (receta.getElementos() != null && !receta.getElementos().isEmpty()) {
                try (PreparedStatement psInsert = conn.prepareStatement(insertElemento)) {
                    for (ElementoReceta elemento : receta.getElementos()) {
                        psInsert.setLong(1, receta.getId());
                        psInsert.setLong(2, elemento.getProducto().getId());
                        psInsert.setDouble(3, elemento.getCantidad());
                        psInsert.addBatch();
                    }
                    psInsert.executeBatch();
                }
            }

            TransactionContext.commit();
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
        return receta;
    }

    @Override
    public void remove(Receta receta) throws SQLException {
        String deleteElementos = "DELETE FROM Elemento_Receta WHERE id_receta = ?";
        String deleteReceta = "DELETE FROM Receta WHERE id_receta = ?";

        Connection conn = TransactionContext.getConnection();

        try {
            try (PreparedStatement psElementos = conn.prepareStatement(deleteElementos)) {
                psElementos.setLong(1, receta.getId());
                psElementos.executeUpdate();
            }

            try (PreparedStatement psReceta = conn.prepareStatement(deleteReceta)) {
                psReceta.setLong(1, receta.getId());
                psReceta.executeUpdate();
            }
            TransactionContext.commit();
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    private List<ElementoReceta> getElementosPorReceta(Connection conn, long idReceta) throws SQLException {
        List<ElementoReceta> elementos = new ArrayList<>();

        String query = "SELECT er.cantidad, p.id_producto, p.nombre, p.precio, p.imagen_url " +
                "FROM Elemento_Receta er " +
                "INNER JOIN Producto p ON er.id_producto = p.id_producto " +
                "WHERE er.id_receta = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, idReceta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setId(rs.getLong("id_producto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setImagenURL(rs.getString("imagen_url"));

                    ElementoReceta er = new ElementoReceta(p, rs.getDouble("cantidad"));
                    elementos.add(er);
                }
            }
        }
        return elementos;
    }
}