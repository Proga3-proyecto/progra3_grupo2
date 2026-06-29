package com.licoreria.dao.catalogo;

import com.licoreria.dao.DAOUtils;
import com.licoreria.dominio.catalogo.Impuesto;
import com.licoreria.dominio.catalogo.TipoImpuesto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ImpuestoDAOImpl implements ImpuestoDAO {
    @Override
    public Impuesto get(Connection con, Integer id) throws SQLException {
        final String sql = "SELECT id_impuesto, nombre,porcentaje,tipo, activo  FROM Impuesto WHERE id_impuesto = ?";
        return DAOUtils.get(sql, con, (ps) -> ps.setInt(1, id), (rs) -> {
            Impuesto impuesto = new Impuesto();
            impuesto.setId(rs.getInt("id_impuesto"));
            impuesto.setNombre(rs.getString("nombre"));
            impuesto.setPorcentaje(rs.getDouble("porcentaje"));
            impuesto.setTipo(TipoImpuesto.valueOf(rs.getString("tipo")));
            impuesto.setActivo(rs.getBoolean("activo"));
            return impuesto;
        });
    }

    @Override
    public List<Impuesto> getAll(Connection con) throws SQLException {
        final String sql = "SELECT id_impuesto, nombre,porcentaje,tipo, activo  FROM Impuesto";
        return DAOUtils.getAll(sql, con, (rs) -> {
            Impuesto impuesto = new Impuesto();
            impuesto.setId(rs.getInt("id_impuesto"));
            impuesto.setNombre(rs.getString("nombre"));
            impuesto.setPorcentaje(rs.getDouble("porcentaje"));
            impuesto.setTipo(TipoImpuesto.valueOf(rs.getString("tipo")));
            impuesto.setActivo(rs.getBoolean("activo"));
            return impuesto;
        });
    }

    @Override
    public Impuesto save(Connection con, Impuesto impuesto) throws SQLException {
        final String sql = "INSERT INTO  Impuesto  (nombre,porcentaje,tipo, activo ) VALUES (?,?,?,?)";
        DAOUtils.save(sql, con, (ps) -> {
            ps.setString(1, impuesto.getNombre());
            ps.setDouble(2, impuesto.getPorcentaje());
            ps.setString(3, impuesto.getTipo().name());
            ps.setBoolean(4, impuesto.getActivo());
        }, (rs) -> {
            impuesto.setId(rs.getInt(1));
        });
        return impuesto;
    }

    @Override
    public Impuesto update(Connection con, Impuesto impuesto) throws SQLException {
        final String sql = "UPDATE Impuesto SET nombre = ?, porcentaje = ?, tipo = ?, activo = ?  WHERE id_impuesto = ? ";
        DAOUtils.update(sql, con, (ps) -> {
            ps.setString(1, impuesto.getNombre());
            ps.setDouble(2, impuesto.getPorcentaje());
            ps.setString(3, impuesto.getTipo().name());
            ps.setBoolean(4, impuesto.getActivo());
            ps.setInt(5, impuesto.getId());
        });
        return impuesto;
    }

    @Override
    public void remove(Connection con, Impuesto impuesto) throws SQLException {
        final String sql = "DELETE FROM Impuesto WHERE id_impuesto = ?";
        DAOUtils.delete(sql,con, (ps)->{
            ps.setInt(1, impuesto.getId());
        });
    }
}
