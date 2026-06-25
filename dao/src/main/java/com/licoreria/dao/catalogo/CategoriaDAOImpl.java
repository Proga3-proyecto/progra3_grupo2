package com.licoreria.dao.catalogo;

import com.licoreria.dao.DAOUtils;
import com.licoreria.dominio.catalogo.Categoria;
import com.licoreria.dominio.catalogo.Producto;
import com.licoreria.dominio.catalogo.Receta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CategoriaDAOImpl implements CategoriaDAO {

    @Override
    public Categoria get(Connection con, Integer id) throws SQLException {
        final String sql = "SELECT id_categoria, nombre FROM Categoria WHERE id_categoria = ?";
        return DAOUtils.get(sql, con, (ps) -> ps.setInt(1, id), (rs) -> {
            Categoria categoria = new Categoria();
            categoria.setId(rs.getInt("id_categoria"));
            categoria.setNombre(rs.getString("nombre"));
            return categoria;
        });
    }

    @Override
    public Categoria get(Connection con, String nombre) throws SQLException {
        final String sql = "SELECT id_categoria, nombre FROM Categoria WHERE nombre = ?";
        return DAOUtils.get(sql, con, (ps) -> ps.setString(1, nombre), (rs) -> {
            Categoria categoria = new Categoria();
            categoria.setId(rs.getInt("id_categoria"));
            categoria.setNombre(rs.getString("nombre"));
            return categoria;
        });
    }

    @Override
    public List<Categoria> getAllByProducto(Connection con, Producto producto) throws SQLException {
        final String sql = "SELECT c.id_categoria, c.nombre " +
                "FROM Categoria c " +
                "INNER JOIN Producto_Categoria pc ON c.id_categoria = pc.id_categoria " +
                "WHERE pc.id_producto = ?";

        return DAOUtils.getAll(sql, con, (ps) -> {
            ps.setInt(1, producto.getId());
        }, (rs) -> {
            Categoria categoria = new Categoria();
            categoria.setId(rs.getInt("id_categoria"));
            categoria.setNombre(rs.getString("nombre"));
            return categoria;
        });
    }

    @Override
    public Map<Integer, List<Categoria>> getAllByProductos(
            Connection con,
            List<Integer> idsProductos
    ) throws SQLException {

        Map<Integer, List<Categoria>> resultado = new HashMap<>();

        if (idsProductos == null || idsProductos.isEmpty()) {
            return resultado;
        }

        String placeholders = idsProductos.stream()
                .map(id -> "?")
                .collect(Collectors.joining(","));

        String sql =
                "SELECT pc.id_producto, c.id_categoria, c.nombre " +
                        "FROM Producto_Categoria pc " +
                        "INNER JOIN Categoria c ON pc.id_categoria = c.id_categoria " +
                        "WHERE pc.id_producto IN (" + placeholders + ")";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            int index = 1;
            for (Integer idProducto : idsProductos) {
                ps.setInt(index++, idProducto);
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Integer idProducto = rs.getInt("id_producto");

                    Categoria categoria = new Categoria();
                    categoria.setId(rs.getInt("id_categoria"));
                    categoria.setNombre(rs.getString("nombre"));

                    resultado
                            .computeIfAbsent(idProducto, k -> new ArrayList<>())
                            .add(categoria);
                }
            }
        }

        return resultado;
    }

    @Override
    public Map<Integer, List<Categoria>> getAllByRecetas(
            Connection con,
            List<Integer> idsProductos
    ) throws SQLException {

        Map<Integer, List<Categoria>> resultado = new HashMap<>();

        if (idsProductos == null || idsProductos.isEmpty()) {
            return resultado;
        }

        String placeholders = idsProductos.stream()
                .map(id -> "?")
                .collect(Collectors.joining(","));


        String sql = "SELECT pc.id_receta, c.id_categoria, c.nombre " +
                "FROM Categoria c " +
                "INNER JOIN Receta_Categoria pc ON c.id_categoria = pc.id_categoria " +
                "WHERE pc.id_receta IN (" + placeholders + ")";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            int index = 1;
            for (Integer idProducto : idsProductos) {
                ps.setInt(index++, idProducto);
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Integer idReceta = rs.getInt("id_receta");

                    Categoria categoria = new Categoria();
                    categoria.setId(rs.getInt("id_categoria"));
                    categoria.setNombre(rs.getString("nombre"));

                    resultado
                            .computeIfAbsent(idReceta, k -> new ArrayList<>())
                            .add(categoria);
                }
            }
        }

        return resultado;
    }

    @Override
    public List<Categoria> getAllByReceta(Connection con, Receta receta) throws SQLException {
        final String sql = "SELECT c.id_categoria, c.nombre " +
                "FROM Categoria c " +
                "INNER JOIN Receta_Categoria pc ON c.id_categoria = pc.id_categoria " +
                "WHERE pc.id_receta = ?";

        return DAOUtils.getAll(sql, con, (ps) -> {
            ps.setInt(1, receta.getId());
        }, (rs) -> {
            Categoria categoria = new Categoria();
            categoria.setId(rs.getInt("id_categoria"));
            categoria.setNombre(rs.getString("nombre"));
            return categoria;
        });
    }

    @Override
    public List<Categoria> getAll(Connection con) throws SQLException {
        final String sql = "SELECT id_categoria, nombre FROM Categoria";
        return DAOUtils.getAll(sql, con, (rs) -> {
            Categoria categoria = new Categoria();
            categoria.setId(rs.getInt("id_categoria"));
            categoria.setNombre(rs.getString("nombre"));
            return categoria;
        });
    }

    @Override
    public Categoria save(Connection con, Categoria categoria) throws SQLException {
        final String sql = "INSERT INTO Categoria (nombre) VALUES (?)";
        DAOUtils.save(sql, con, (ps) -> {
            ps.setString(1, categoria.getNombre());
        }, (rs) -> {
            categoria.setId(rs.getInt(1));
        });
        return categoria;
    }

    @Override
    public Categoria update(Connection con, Categoria categoria) throws SQLException {
        final String sql = "UPDATE Categoria SET nombre = ? WHERE id_categoria = ?";
        DAOUtils.update(sql, con, (ps) -> {
            ps.setString(1, categoria.getNombre());
            ps.setInt(2, categoria.getId());
        });
        return categoria;
    }

    @Override
    public void remove(Connection con, Categoria categoria) throws SQLException {
        final String sql = "DELETE FROM Categoria WHERE id_categoria = ?";
        DAOUtils.delete(sql, con, (ps) -> {
            ps.setInt(1, categoria.getId());
        });
    }


}