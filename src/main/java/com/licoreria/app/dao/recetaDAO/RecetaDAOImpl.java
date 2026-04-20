package com.licoreria.app.dao.recetaDAO;

import com.licoreria.app.dao.conexionBD.ConexionDB;
import com.licoreria.app.modelo.productos.ElementoReceta;
import com.licoreria.app.modelo.productos.Producto;
import com.licoreria.app.modelo.productos.Receta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecetaDAOImpl implements RecetaDAO {

    @Override
    public Receta get(long id) {
        Receta receta = null;
        String queryReceta = "SELECT * FROM Receta WHERE id_receta = ?";

        try (Connection conn = ConexionDB.getInstance().getConexion();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return receta;
    }

    @Override
    public List<Receta> getAll() {
        List<Receta> recetas = new ArrayList<>();
        String query = "SELECT * FROM Receta";

        try (Connection conn = ConexionDB.getInstance().getConexion();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recetas;
    }

    @Override
    public void save(Receta receta) {
        String insertReceta = "INSERT INTO Receta (nombre, descripcion, image_src) VALUES (?, ?, ?)";
        String insertElemento = "INSERT INTO Elemento_Receta (id_receta, id_producto, cantidad) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = ConexionDB.getInstance().getConexion();
            conn.setAutoCommit(false);

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

                        if (receta.getProductos() != null && !receta.getProductos().isEmpty()) {
                            try (PreparedStatement psElemento = conn.prepareStatement(insertElemento)) {
                                for (ElementoReceta elemento : receta.getProductos()) {
                                    psElemento.setLong(1, idReceta);
                                    // Asume que la clase Producto tiene un método getId()
                                    psElemento.setLong(2, elemento.getProducto().getId());
                                    psElemento.setDouble(3, elemento.getCantidad());
                                    psElemento.addBatch();
                                }
                                psElemento.executeBatch(); // Ejecutar inserciones en lote
                            }
                        }
                    } else {
                        throw new SQLException("Fallo al crear la receta, no se obtuvo el ID.");
                    }
                }
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void update(Receta receta) {
        String updateReceta = "UPDATE Receta SET nombre=?, descripcion=?, image_src=? WHERE id_receta=?";
        String deleteElementos = "DELETE FROM Elemento_Receta WHERE id_receta=?";
        String insertElemento = "INSERT INTO Elemento_Receta (id_receta, id_producto, cantidad) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = ConexionDB.getInstance().getConexion();
            conn.setAutoCommit(false);

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

            if (receta.getProductos() != null && !receta.getProductos().isEmpty()) {
                try (PreparedStatement psInsert = conn.prepareStatement(insertElemento)) {
                    for (ElementoReceta elemento : receta.getProductos()) {
                        psInsert.setLong(1, receta.getId());
                        psInsert.setLong(2, elemento.getProducto().getId());
                        psInsert.setDouble(3, elemento.getCantidad());
                        psInsert.addBatch();
                    }
                    psInsert.executeBatch();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void delete(Receta receta) {
        String query = "DELETE FROM Receta WHERE id_receta = ?";
        try (Connection conn = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setLong(1, receta.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private List<ElementoReceta> getElementosPorReceta(Connection conn, long idReceta) throws SQLException {
        List<ElementoReceta> elementos = new ArrayList<>();

        String query = "SELECT er.cantidad, p.id_producto, p.nombre, p.precio,p.imagen_url" +
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