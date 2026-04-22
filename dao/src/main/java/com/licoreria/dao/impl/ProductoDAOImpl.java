package com.licoreria.dao.impl;

import com.licoreria.DBmanager.DBManager;
import com.licoreria.DBmanager.TransactionContext;
import com.licoreria.dao.ProductoDAO;
import com.licoreria.dominio.productos.Impuesto;
import com.licoreria.dominio.productos.Producto;
import com.licoreria.dominio.productos.TipoImpuesto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {

    @Override
    public Producto get(Long id) throws SQLException {
        Producto producto = null;
        String query = "SELECT * FROM Producto WHERE id_producto = ?";

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    producto = mapResultSetToProducto(rs);
                    producto.setImpuestos(getImpuestosPorProducto(conn, id));
                }
            }
        }
        return producto;
    }

    @Override
    public List<Producto> getAll() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String query = "SELECT * FROM Producto";

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = mapResultSetToProducto(rs);
                p.setImpuestos(getImpuestosPorProducto(conn, p.getId()));
                productos.add(p);
            }
        }
        return productos;
    }

    @Override
    public Producto save(Producto producto) throws SQLException {
        String insertQuery = "INSERT INTO Producto (nombre, precio, stock, descuento, imagen_url, volumen_litros, porcentaje_alcohol) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertImpuestos = "INSERT INTO Producto_Impuesto (id_producto, id_impuesto) VALUES (?, ?)";

        Connection conn = TransactionContext.getConnection();

        try {
            try (PreparedStatement ps = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, producto.getNombre());
                ps.setDouble(2, producto.getPrecio());
                ps.setInt(3, producto.getStock());
                ps.setDouble(4, producto.getDescuento());
                ps.setString(5, producto.getImagenURL());

                if (producto.getVolumenLitros() != null) {
                    ps.setDouble(6, producto.getVolumenLitros());
                } else {
                    ps.setNull(6, java.sql.Types.DECIMAL);
                }

                if (producto.getPorcentajeAlcohol() != null) {
                    ps.setDouble(7, producto.getPorcentajeAlcohol());
                } else {
                    ps.setNull(7, java.sql.Types.DECIMAL);
                }


                ps.executeUpdate();

                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long idGenerado = generatedKeys.getLong(1);
                        producto.setId(idGenerado);

                        if (producto.getImpuestos() != null && !producto.getImpuestos().isEmpty()) {
                            try (PreparedStatement psImp = conn.prepareStatement(insertImpuestos)) {
                                for (Impuesto imp : producto.getImpuestos()) {
                                    psImp.setLong(1, idGenerado);
                                    psImp.setLong(2, imp.getId());
                                    psImp.addBatch();
                                }
                                psImp.executeBatch();
                            }
                        }
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
        return producto;
    }

    @Override
    public Producto update(Producto producto) throws SQLException {
        String updateQuery = "UPDATE Producto SET nombre=?, precio=?, stock=?, descuento=?, imagen_url=?, volumen_litros=?, porcentaje_alcohol=? WHERE id_producto=?";
        String deleteImpuestos = "DELETE FROM Producto_Impuesto WHERE id_producto=?";
        String insertImpuestos = "INSERT INTO Producto_Impuesto (id_producto, id_impuesto) VALUES (?, ?)";

        Connection conn = TransactionContext.getConnection();

        try {
            try (PreparedStatement ps = conn.prepareStatement(updateQuery)) {
                ps.setString(1, producto.getNombre());
                ps.setDouble(2, producto.getPrecio());
                ps.setInt(3, producto.getStock());
                ps.setDouble(4, producto.getDescuento());
                ps.setString(5, producto.getImagenURL());

                if (producto.getVolumenLitros() != null) {
                    ps.setDouble(6, producto.getVolumenLitros());
                } else {
                    ps.setNull(6, java.sql.Types.DECIMAL);
                }

                if (producto.getPorcentajeAlcohol() != null) {
                    ps.setDouble(7, producto.getPorcentajeAlcohol());
                } else {
                    ps.setNull(7, java.sql.Types.DECIMAL);
                }

                ps.setLong(8, producto.getId());
                ps.executeUpdate();
            }

            try (PreparedStatement psDel = conn.prepareStatement(deleteImpuestos)) {
                psDel.setLong(1, producto.getId());
                psDel.executeUpdate();
            }

            if (producto.getImpuestos() != null && !producto.getImpuestos().isEmpty()) {
                try (PreparedStatement psImp = conn.prepareStatement(insertImpuestos)) {
                    for (Impuesto imp : producto.getImpuestos()) {
                        psImp.setLong(1, producto.getId());
                        psImp.setLong(2, imp.getId());
                        psImp.addBatch();
                    }
                    psImp.executeBatch();
                }
            }
            TransactionContext.commit();
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
        return producto;
    }

    @Override
    public void remove(Producto producto) throws SQLException {
        String deleteQuery = "DELETE FROM Producto WHERE id_producto = ?";
        Connection conn = TransactionContext.getConnection();

        try {
            try (PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
                ps.setLong(1, producto.getId());
                ps.executeUpdate();
            }
            TransactionContext.commit();
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
    }

    private List<Impuesto> getImpuestosPorProducto(Connection conn, long idProducto) throws SQLException {
        List<Impuesto> impuestos = new ArrayList<>();
        String query = "SELECT i.* FROM Impuesto i INNER JOIN Producto_Impuesto pi ON i.id_impuesto = pi.id_impuesto WHERE pi.id_producto = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Impuesto i = new Impuesto();
                    i.setId(rs.getLong("id_impuesto"));
                    i.setNombre(rs.getString("nombre"));
                    i.setValor(rs.getDouble("valor"));
                    i.setTipo(TipoImpuesto.valueOf(rs.getString("tipo")));
                    i.setActivo(rs.getBoolean("activo"));
                    impuestos.add(i);
                }
            }
        }
        return impuestos;
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

        double volumen = rs.getDouble("volumen_litros");
        if (!rs.wasNull()) {
            p.setVolumenLitros(volumen);
        }

        double porcentaje = rs.getDouble("porcentaje_alcohol");
        if (!rs.wasNull()) {
            p.setPorcentajeAlcohol(porcentaje);
        }

        return p;
    }
}