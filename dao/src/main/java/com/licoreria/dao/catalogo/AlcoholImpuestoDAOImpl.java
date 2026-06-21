package com.licoreria.dao.catalogo;

import com.licoreria.dao.DAOUtils;
import com.licoreria.dominio.catalogo.AlcoholImpuesto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AlcoholImpuestoDAOImpl implements AlcoholImpuestoDAO {

    @Override
    public AlcoholImpuesto get(Connection con, Integer id) throws SQLException {
        final String sql = "SELECT id_alcohol_impuesto, minimo, maximo, porcentaje_precio, valor FROM AlcoholImpuesto WHERE id_alcohol_impuesto = ?";
        return DAOUtils.get(sql, con, (ps) -> ps.setInt(1, id), (rs) -> {
            AlcoholImpuesto alcoholImpuesto = new AlcoholImpuesto();
            alcoholImpuesto.setId(rs.getInt("id_alcohol_impuesto"));
            alcoholImpuesto.setMinimo(rs.getInt("minimo"));
            alcoholImpuesto.setMaximo(rs.getInt("maximo"));
            alcoholImpuesto.setPorcentajePrecio(rs.getInt("porcentaje_precio"));
            alcoholImpuesto.setValor(rs.getDouble("valor"));
            return alcoholImpuesto;
        });
    }

    @Override
    public List<AlcoholImpuesto> getAll(Connection con) throws SQLException {
        final String sql = "SELECT id_alcohol_impuesto, minimo, maximo, porcentaje_precio, valor FROM AlcoholImpuesto";
        return DAOUtils.getAll(sql, con, (rs) -> {
            AlcoholImpuesto alcoholImpuesto = new AlcoholImpuesto();
            alcoholImpuesto.setId(rs.getInt("id_alcohol_impuesto"));
            alcoholImpuesto.setMinimo(rs.getInt("minimo"));
            alcoholImpuesto.setMaximo(rs.getInt("maximo"));
            alcoholImpuesto.setPorcentajePrecio(rs.getInt("porcentaje_precio"));
            alcoholImpuesto.setValor(rs.getDouble("valor"));
            return alcoholImpuesto;
        });
    }

    @Override
    public AlcoholImpuesto save(Connection con, AlcoholImpuesto alcoholImpuesto) throws SQLException {
        final String sql = "INSERT INTO AlcoholImpuesto (minimo, maximo, porcentaje_precio, valor) VALUES (?, ?, ?, ?)";
        DAOUtils.save(sql, con, (ps) -> {
            ps.setInt(1, alcoholImpuesto.getMinimo());
            ps.setInt(2, alcoholImpuesto.getMaximo());
            ps.setInt(3, alcoholImpuesto.getPorcentajePrecio());
            ps.setDouble(4, alcoholImpuesto.getValor());
        }, (rs) -> {
            alcoholImpuesto.setId(rs.getInt(1));
        });
        return alcoholImpuesto;
    }

    @Override
    public AlcoholImpuesto update(Connection con, AlcoholImpuesto alcoholImpuesto) throws SQLException {
        final String sql = "UPDATE AlcoholImpuesto SET minimo = ?, maximo = ?, porcentaje_precio = ?, valor = ? WHERE id_alcohol_impuesto = ?";
        DAOUtils.update(sql, con, (ps) -> {
            ps.setInt(1, alcoholImpuesto.getMinimo());
            ps.setInt(2, alcoholImpuesto.getMaximo());
            ps.setInt(3, alcoholImpuesto.getPorcentajePrecio());
            ps.setDouble(4, alcoholImpuesto.getValor());
            ps.setInt(5, alcoholImpuesto.getId());
        });
        return alcoholImpuesto;
    }

    @Override
    public void remove(Connection con, AlcoholImpuesto alcoholImpuesto) throws SQLException {
        final String sql = "DELETE FROM AlcoholImpuesto WHERE id_alcohol_impuesto = ?";
        DAOUtils.delete(sql, con, (ps) -> {
            ps.setInt(1, alcoholImpuesto.getId());
        });
    }
}