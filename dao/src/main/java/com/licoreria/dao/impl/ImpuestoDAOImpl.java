package com.licoreria.dao.impl;

import com.licoreria.DBmanager.DBManager;
import com.licoreria.DBmanager.TransactionContext;
import com.licoreria.dao.ImpuestoDAO;
import com.licoreria.dominio.productos.Impuesto;
import com.licoreria.dominio.productos.TipoImpuesto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImpuestoDAOImpl implements ImpuestoDAO {

    @Override
    public Impuesto get(Long id) throws SQLException {
        Impuesto impuesto = null;
        String query = "SELECT * FROM Impuesto WHERE id_impuesto = ?";

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    impuesto = mapResultSetToImpuesto(rs);
                }
            }
        }
        return impuesto;
    }

    @Override
    public List<Impuesto> getAll() throws SQLException {
        List<Impuesto> impuestos = new ArrayList<>();
        String query = "SELECT * FROM Impuesto";

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                impuestos.add(mapResultSetToImpuesto(rs));
            }
        }
        return impuestos;
    }

    @Override
    public Impuesto save(Impuesto impuesto) throws SQLException {
        String query = "INSERT INTO Impuesto (nombre, valor, tipo, activo) VALUES (?, ?, ?, ?)";
        Connection conn = TransactionContext.getConnection();

        try {
            try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, impuesto.getNombre());
                ps.setDouble(2, impuesto.getValor());
                ps.setString(3, impuesto.getTipo().name());
                ps.setBoolean(4, impuesto.getActivo() != null ? impuesto.getActivo() : true);

                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        impuesto.setId(rs.getLong(1));
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
        return impuesto;
    }

    @Override
    public Impuesto update(Impuesto impuesto) throws SQLException {
        String query = "UPDATE Impuesto SET nombre=?, valor=?, tipo=?, activo=? WHERE id_impuesto=?";
        Connection conn = TransactionContext.getConnection();

        try {
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, impuesto.getNombre());
                ps.setDouble(2, impuesto.getValor());
                ps.setString(3, impuesto.getTipo().name());
                ps.setBoolean(4, impuesto.getActivo());
                ps.setLong(5, impuesto.getId());
                ps.executeUpdate();
            }
            TransactionContext.commit();
        } catch (SQLException e) {
            TransactionContext.rollback();
            throw e;
        } finally {
            TransactionContext.close();
        }
        return impuesto;
    }

    @Override
    public void remove(Impuesto impuesto) throws SQLException {
        String query = "DELETE FROM Impuesto WHERE id_impuesto = ?";
        Connection conn = TransactionContext.getConnection();

        try {
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setLong(1, impuesto.getId());
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

    private Impuesto mapResultSetToImpuesto(ResultSet rs) throws SQLException {
        Impuesto i = new Impuesto();
        i.setId(rs.getLong("id_impuesto"));
        i.setNombre(rs.getString("nombre"));
        i.setValor(rs.getDouble("valor"));
        i.setTipo(TipoImpuesto.valueOf(rs.getString("tipo")));
        i.setActivo(rs.getBoolean("activo"));
        return i;
    }
}