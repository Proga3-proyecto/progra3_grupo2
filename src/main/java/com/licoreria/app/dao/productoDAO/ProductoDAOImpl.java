package com.licoreria.app.dao.productoDAO;

import com.licoreria.app.dao.conexionBD.ConexionDB;
import com.licoreria.app.modelo.productos.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {

    @Override
    public Producto get(long id) {
        Producto producto = null;
        String query = "SELECT * FROM Producto WHERE id_producto = ?";

        try (Connection conn = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    producto = mapResultSetToProducto(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return producto;
    }

    @Override
    public List<Producto> getAll() {
        List<Producto> productos = new ArrayList<>();
        String query = "SELECT * FROM Producto";

        try (Connection conn = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                productos.add(mapResultSetToProducto(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    @Override
    public void save(Producto producto) {

        String insertQuery = "INSERT INTO Producto (nombre, precio, stock, descuento, imagen_url) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getStock());
            ps.setDouble(4, producto.getDescuento());
            ps.setString(5, producto.getImagenURL());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Fallo al crear el producto, no se afectaron filas.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    producto.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Fallo al crear el producto, no se obtuvo el ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Producto producto) {
        String updateQuery = "UPDATE Producto SET nombre=?, precio=?, stock=?, descuento=?, imagen_url=? WHERE id_producto=?";

        try (Connection conn = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = conn.prepareStatement(updateQuery)) {

            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getStock());
            ps.setDouble(4, producto.getDescuento());
            ps.setString(5, producto.getImagenURL());
            ps.setLong(6, producto.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Producto producto) {
        String deleteQuery = "DELETE FROM Producto WHERE id_producto = ?";

        try (Connection conn = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = conn.prepareStatement(deleteQuery)) {

            ps.setLong(1, producto.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Producto mapResultSetToProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto(
                rs.getString("nombre"),
                rs.getString("imagen_url"),
                rs.getDouble("precio"),
                rs.getInt("stock"),
                rs.getDouble("descuento")
        );

        p.setId(rs.getLong("id_producto"));

        return p;
    }
}