package com.licoreria.dao.catalogo;

import com.licoreria.dao.DAOUtils;
import com.licoreria.dominio.catalogo.Imagen;
import com.licoreria.dominio.catalogo.Marca;
import com.licoreria.dominio.catalogo.Producto;
import com.licoreria.dominio.catalogo.Receta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class ImagenDAOImpl implements ImagenDAO {
    @Override
    public Imagen get(Connection con, Integer id) throws SQLException {
        final String sql = "SELECT id_imagen, url FROM Imagen WHERE id_imagen = ?";
        return DAOUtils.get(sql, con,
                (ps) -> ps.setInt(1, id),
                (rs) -> {
                    Imagen imagen = new Imagen();
                    imagen.setId(rs.getInt("id_imagen"));
                    imagen.setUrl(rs.getString("url"));
                    return imagen;
                }
        );
    }

    @Override
    public List<Imagen> getAll(Connection con) throws SQLException {
        final String sql = "SELECT id_imagen, url FROM Imagen";
        return DAOUtils.getAll(sql, con, (rs) -> {
            Imagen imagen = new Imagen();
            imagen.setId(rs.getInt("id_imagen"));
            imagen.setUrl(rs.getString("url"));
            return imagen;
        });
    }


    @Override
    public Imagen save(Connection con, Imagen imagen) throws SQLException {
        final String sql = "INSERT INTO Imagen (url) VALUES (?)";
        DAOUtils.save(sql, con,
                (ps) -> {
                    ps.setString(1, imagen.getUrl());
                }, (rs) -> {
                    imagen.setId(rs.getInt(1));
                }
        );
        return imagen;
    }

    @Override
    public Imagen update(Connection con, Imagen imagen) throws SQLException {
        final String sql = "UPDATE Imagen SET url = ? WHERE id_imagen = ? ";
        DAOUtils.update(sql, con,
                (ps) -> {
                    ps.setString(1, imagen.getUrl());
                    ps.setInt(2, imagen.getId());
                }
        );
        return imagen;
    }

    @Override
    public void remove(Connection con, Imagen imagen) throws SQLException {
        // CORREGIDO: Sintaxis SQL incorrecta (DELETE FROM Imagen FROM -> DELETE FROM Imagen WHERE)
        final String sql = "DELETE FROM Imagen WHERE id_imagen = ?";
        DAOUtils.delete(sql, con, (ps) -> {
            ps.setInt(1, imagen.getId());
        });
    }

    @Override
    public Imagen get(Connection con, String url) throws SQLException {
        final String sql = "SELECT id_imagen, url FROM Imagen WHERE url = ?";
        return DAOUtils.get(sql, con,
                (ps) -> ps.setString(1, url),
                (rs) -> {
                    Imagen imagen = new Imagen();
                    imagen.setId(rs.getInt("id_imagen"));
                    imagen.setUrl(rs.getString("url"));
                    return imagen;
                }
        );
    }

    @Override
    public List<Imagen> getAllByProduct(Connection con, Producto producto) throws SQLException {
        final String sql = "SELECT i.id_imagen, i.url, pi.principal " +
                "FROM ProductoImagen pi " +
                "INNER JOIN Imagen i ON pi.id_imagen = i.id_imagen " +
                "WHERE pi.id_producto = ?";
        return DAOUtils.getAll(sql, con, (ps) -> {
            ps.setInt(1, producto.getId());
        }, (rs) -> {
            Imagen imagen = new Imagen();
            imagen.setId(rs.getInt("id_imagen"));
            imagen.setUrl(rs.getString("url"));
            imagen.setPrincipal(rs.getBoolean("principal"));
            return imagen;
        });
    }

    @Override
    public Map<Integer, List<Imagen>> getAllByProducts(
            Connection con,
            List<Integer> idsProducto
    ) throws SQLException {

        if (idsProducto == null || idsProducto.isEmpty()) {
            return new HashMap<>();
        }

        String placeholders = idsProducto.stream()
                .map(id -> "?")
                .collect(Collectors.joining(","));

        String sql =
                "SELECT pi.id_producto, i.id_imagen, i.url, pi.principal " +
                        "FROM ProductoImagen pi " +
                        "INNER JOIN Imagen i ON pi.id_imagen = i.id_imagen " +
                        "WHERE pi.id_producto IN (" + placeholders + ")";

        Map<Integer, List<Imagen>> resultado = new HashMap<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            int index = 1;
            for (Integer idProducto : idsProducto) {
                ps.setInt(index++, idProducto);
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Integer idProducto = rs.getInt("id_producto");

                    Imagen imagen = new Imagen();
                    imagen.setId(rs.getInt("id_imagen"));
                    imagen.setUrl(rs.getString("url"));
                    imagen.setPrincipal(rs.getBoolean("principal"));

                    resultado
                            .computeIfAbsent(idProducto, k -> new ArrayList<>())
                            .add(imagen);
                }
            }
        }

        return resultado;
    }

    @Override
    public Map<Integer, List<Imagen>> getAllByRecetas(
            Connection con,
            List<Integer> idsRecetas
    ) throws SQLException {

        if (idsRecetas == null || idsRecetas.isEmpty()) {
            return new HashMap<>();
        }

        String placeholders = idsRecetas.stream()
                .map(id -> "?")
                .collect(Collectors.joining(","));

        // CORREGIDO: Se agregó ri.id_receta al SELECT
        String sql = "SELECT ri.id_receta, i.id_imagen, i.url, ri.principal " +
                "FROM RecetaImagen ri " +
                "INNER JOIN Imagen i ON ri.id_imagen = i.id_imagen " +
                "WHERE ri.id_receta IN (" + placeholders + ")";

        Map<Integer, List<Imagen>> resultado = new HashMap<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            int index = 1;
            // CORREGIDO: Renombrado idProducto a idReceta para claridad
            for (Integer idReceta : idsRecetas) {
                ps.setInt(index++, idReceta);
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Integer idReceta = rs.getInt("id_receta");

                    Imagen imagen = new Imagen();
                    imagen.setId(rs.getInt("id_imagen"));
                    imagen.setUrl(rs.getString("url"));
                    imagen.setPrincipal(rs.getBoolean("principal"));

                    resultado
                            .computeIfAbsent(idReceta, k -> new ArrayList<>())
                            .add(imagen);
                }
            }
        }

        return resultado;
    }

    @Override
    public Map<Integer, Imagen> getMapByIds(Connection con, List<Integer> ids) throws SQLException {
        String placeholders = ids.stream()
                .map(id -> "?")
                .collect(Collectors.joining(","));
        Map<Integer, Imagen> resultado = new HashMap<>();
        final String sql = "SELECT id_imagen, url FROM Imagen WHERE id_imagen in (" + placeholders + ")";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            int index = 1;
            for (Integer id : ids) {
                ps.setInt(index++, id);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Imagen imagen = new Imagen();
                    imagen.setId(rs.getInt("id_imagen"));
                    imagen.setUrl(rs.getString("url"));

                    resultado.put(imagen.getId(), imagen);
                }
            }
        }
        return resultado;
    }

    @Override
    public List<Imagen> getAllByReceta(Connection con, Receta receta) throws SQLException {
        final String sql = "SELECT i.id_imagen, i.url " +
                "FROM RecetaImagen ri " +
                "INNER JOIN Imagen i ON ri.id_imagen = i.id_imagen " +
                "WHERE ri.id_receta = ?";

        return DAOUtils.getAll(sql, con, (ps) -> {
            ps.setInt(1, receta.getId());
        }, (rs) -> {
            Imagen imagen = new Imagen();
            imagen.setId(rs.getInt("id_imagen"));
            imagen.setUrl(rs.getString("url"));
            return imagen;
        });
    }
}