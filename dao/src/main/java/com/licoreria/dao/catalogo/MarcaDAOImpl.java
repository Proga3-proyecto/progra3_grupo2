package com.licoreria.dao.catalogo;

import com.licoreria.dao.DAOUtils;
import com.licoreria.dominio.catalogo.Marca;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MarcaDAOImpl implements MarcaDAO {

    @Override
    public Marca get(Connection con, Integer id) throws SQLException {
        final String sql = "SELECT id_marca, nombre FROM  Marca WHERE id_marca = ?";
        return DAOUtils.get(sql, con,
                (ps) -> ps.setInt(1, id),
                (rs) -> {
                    Marca marca = new Marca();
                    marca.setId(rs.getInt("id_marca"));
                    marca.setNombre(rs.getString("nombre"));
                    return marca;
                }
        );
    }


    @Override
    public Marca get(Connection con, String nombre) throws SQLException {
        final String sql = "SELECT id_marca, nombre FROM  Marca WHERE nombre = ?";
        return DAOUtils.get(sql, con,
                (ps) -> ps.setString(1, nombre),
                (rs) -> {
                    Marca marca = new Marca();
                    marca.setId(rs.getInt("id_marca"));
                    marca.setNombre(rs.getString("nombre"));
                    return marca;
                }
        );
    }

    @Override
    public List<Marca> getAll(Connection con) throws SQLException {
        final String sql = "SELECT id_marca, nombre FROM  Marca";
        return DAOUtils.getAll(sql, con, (rs) -> {
            Marca marca = new Marca();
            marca.setId(rs.getInt("id_marca"));
            marca.setNombre(rs.getString("nombre"));
            return marca;
        });
    }


    @Override
    public Marca save(Connection con, Marca marca) throws SQLException {
        final String sql = "INSERT INTO  Marca  (nombre) VALUES (?)";
        DAOUtils.save(sql, con,
                (ps) -> {
                    ps.setString(1, marca.getNombre());
                }, (rs) -> {
                    marca.setId(rs.getInt(1));
                }
        );
        return marca;
    }

    @Override
    public Marca update(Connection con, Marca marca) throws SQLException {
        final String sql = "UPDATE Marca SET nombre = ? WHERE id_marca = ? ";
        DAOUtils.update(sql, con,
                (ps) -> {
                    ps.setString(1, marca.getNombre());
                    ps.setInt(2, marca.getId());
                }
        );
        return marca;
    }

    @Override
    public void remove(Connection con, Marca marca) throws SQLException {
        final String sql = "DELETE FROM  Marca FROM  id_marca = ?";
        DAOUtils.delete(sql, con, (ps) -> {
            ps.setInt(1, marca.getId());
        });
    }

}
