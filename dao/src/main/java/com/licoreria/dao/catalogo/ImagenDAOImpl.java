package com.licoreria.dao.catalogo;

import com.licoreria.dao.DAOUtils;
import com.licoreria.dominio.catalogo.Imagen;
import com.licoreria.dominio.catalogo.Marca;
import com.licoreria.dominio.catalogo.Producto;
import com.licoreria.dominio.catalogo.Receta;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
        final String sql = "INSERT INTO  Imagen  (url) VALUES (?)";
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
        final String sql = "DELETE FROM  Imagen FROM  id_imagen = ?";
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
        final String sql = "SELECT i.id_imagen, i.url " +
                "FROM ProductoImagen pi " +
                "INNER JOIN Imagen i ON pi.id_imagen = i.id_imagen " +
                "WHERE pi.id_producto = ?";
        return DAOUtils.getAll(sql, con, (ps) -> {
            ps.setInt(1, producto.getId());
        }, (rs) -> {
            Imagen imagen = new Imagen();
            imagen.setId(rs.getInt("id_imagen"));
            imagen.setUrl(rs.getString("url"));
            return imagen;
        });
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
