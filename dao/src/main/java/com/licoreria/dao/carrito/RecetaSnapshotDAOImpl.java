package com.licoreria.dao.carrito;

import com.licoreria.dao.DAOUtils;
import com.licoreria.dominio.Snapshots.ProductoSnapshot;
import com.licoreria.dominio.Snapshots.RecetaSnapshot;
import com.licoreria.dominio.Snapshots.RecetaSnapshotElemento;
import com.licoreria.dominio.catalogo.Imagen;
import com.licoreria.dominio.catalogo.Receta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecetaSnapshotDAOImpl implements RecetaSnapshotDAO {

    @Override
    public RecetaSnapshot get(Connection con, Integer id) throws SQLException {
        final String sql = "SELECT rs.id_receta_snapshot, rs.id_receta_original, rs.nombre, " +
                "rs.precio_historico, rs.precio_final_historico, rs.id_imagen, i.url AS imagen_url " +
                "FROM Receta_Snapshot rs " +
                "LEFT JOIN Imagen i ON rs.id_imagen = i.id_imagen " +
                "WHERE rs.id_receta_snapshot = ?";

        RecetaSnapshot snapshot = DAOUtils.get(sql, con, (ps) -> ps.setInt(1, id), this::mapearSnapshot);

        if (snapshot != null) {
            cargarElementos(con, snapshot);
        }
        return snapshot;
    }

    @Override
    public List<RecetaSnapshot> getAll(Connection con) throws SQLException {
        final String sql = "SELECT rs.id_receta_snapshot, rs.id_receta_original, rs.nombre, " +
                "rs.precio_historico, rs.precio_final_historico, rs.id_imagen, i.url AS imagen_url " +
                "FROM Receta_Snapshot rs " +
                "LEFT JOIN Imagen i ON rs.id_imagen = i.id_imagen ";

        List<RecetaSnapshot> snapshots = DAOUtils.getAll(sql, con, this::mapearSnapshot);

        for (RecetaSnapshot snapshot : snapshots) {
            cargarElementos(con, snapshot);
        }
        return snapshots;
    }

    @Override
    public RecetaSnapshot save(Connection con, RecetaSnapshot snapshot) throws SQLException {
        final String sql = "INSERT INTO Receta_Snapshot (id_receta_original, nombre, " +
                "precio_historico, precio_final_historico, id_imagen) VALUES (?, ?, ?, ?, ?)";

        DAOUtils.save(sql, con, (ps) -> prepararDeclaracion(ps, snapshot), (rs) -> snapshot.setId(rs.getInt(1)));

        if (snapshot.getId() != null) {
            guardarElementos(con, snapshot);
        }
        return snapshot;
    }

    @Override
    public RecetaSnapshot update(Connection con, RecetaSnapshot snapshot) throws SQLException {
        final String sql = "UPDATE Receta_Snapshot SET id_receta_original = ?, nombre = ?, " +
                "precio_historico = ?, precio_final_historico = ?, id_imagen = ? " +
                "WHERE id_receta_snapshot = ?";

        DAOUtils.update(sql, con, (ps) -> {
            prepararDeclaracion(ps, snapshot);
            ps.setInt(6, snapshot.getId());
        });

        eliminarElementos(con, snapshot.getId());
        guardarElementos(con, snapshot);

        return snapshot;
    }

    @Override
    public void remove(Connection con, RecetaSnapshot snapshot) throws SQLException {
        eliminarElementos(con, snapshot.getId());

        final String sql = "DELETE FROM Receta_Snapshot WHERE id_receta_snapshot = ?";
        DAOUtils.delete(sql, con, (ps) -> ps.setInt(1, snapshot.getId()));
    }

    @Override
    public Map<Integer, RecetaSnapshot> getMapByIds(Connection con, List<Integer> idsSnapshots) throws SQLException {
        Map<Integer, RecetaSnapshot> resultado = new HashMap<>();
        if (idsSnapshots == null || idsSnapshots.isEmpty()) return resultado;

        String placeholders = idsSnapshots.stream().map(id -> "?").collect(Collectors.joining(","));
        String sql = "SELECT rs.id_receta_snapshot, rs.id_receta_original, rs.nombre, " +
                "rs.precio_historico, rs.precio_final_historico, rs.id_imagen, i.url AS imagen_url " +
                "FROM Receta_Snapshot rs " +
                "LEFT JOIN Imagen i ON rs.id_imagen = i.id_imagen " +
                "WHERE rs.id_receta_snapshot IN (" + placeholders + ")";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            int index = 1;
            for (Integer id : idsSnapshots) {
                ps.setInt(index++, id);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RecetaSnapshot snapshot = mapearSnapshot(rs);
                    resultado.put(snapshot.getId(), snapshot);
                }
            }
        }

        for (RecetaSnapshot snapshot : resultado.values()) {
            cargarElementos(con, snapshot);
        }

        return resultado;
    }

    private void cargarElementos(Connection con, RecetaSnapshot snapshot) throws SQLException {
        final String sql = "SELECT id_producto_snapshot, cantidad FROM Receta_Snapshot_Elemento WHERE id_receta_snapshot = ?";

        List<RecetaSnapshotElemento> elementos = DAOUtils.getAll(sql, con,
                (ps) -> ps.setInt(1, snapshot.getId()),
                (rs) -> {
                    RecetaSnapshotElemento elemento = new RecetaSnapshotElemento();
                    elemento.setCantidad(rs.getDouble("cantidad"));

                    ProductoSnapshot productoSnapshot = new ProductoSnapshot();
                    productoSnapshot.setId(rs.getInt("id_producto_snapshot"));
                    elemento.setProductoSnapshot(productoSnapshot);

                    return elemento;
                }
        );
        snapshot.setElementosHistoricos(elementos);
    }

    private void guardarElementos(Connection con, RecetaSnapshot snapshot) throws SQLException {
        if (snapshot.getElementosHistoricos() == null || snapshot.getElementosHistoricos().isEmpty()) return;

        ProductoSnapshotDAO productoSnapshotDAO = new ProductoSnapshotDAOImpl();
        final String sql = "INSERT INTO Receta_Snapshot_Elemento (id_receta_snapshot, id_producto_snapshot, cantidad) VALUES (?, ?, ?)";

        for (RecetaSnapshotElemento elemento : snapshot.getElementosHistoricos()) {
            if (elemento.getProductoSnapshot() != null && (elemento.getProductoSnapshot().getId() == null || elemento.getProductoSnapshot().getId() == 0)) {
                productoSnapshotDAO.save(con, elemento.getProductoSnapshot());
            }

            DAOUtils.save(sql, con, (ps) -> {
                ps.setInt(1, snapshot.getId());
                ps.setInt(2, elemento.getProductoSnapshot().getId());
                ps.setDouble(3, elemento.getCantidad());
            }, (rs) -> {});
        }
    }

    private void eliminarElementos(Connection con, Integer idSnapshot) throws SQLException {
        final String sql = "DELETE FROM Receta_Snapshot_Elemento WHERE id_receta_snapshot = ?";
        DAOUtils.delete(sql, con, (ps) -> ps.setInt(1, idSnapshot));
    }

    private RecetaSnapshot mapearSnapshot(ResultSet rs) throws SQLException {
        RecetaSnapshot snapshot = new RecetaSnapshot();
        snapshot.setId(rs.getInt("id_receta_snapshot"));

        int idOriginal = rs.getInt("id_receta_original");
        if (!rs.wasNull()) {
            Receta recetaOriginal = new Receta();
            recetaOriginal.setId(idOriginal);
            snapshot.setRecetaOriginal(recetaOriginal);
        }

        snapshot.setNombre(rs.getString("nombre"));
        snapshot.setPrecioHistorico(rs.getDouble("precio_historico"));
        snapshot.setPrecioFinalHistorico(rs.getDouble("precio_final_historico"));

        int idImagen = rs.getInt("id_imagen");
        if (!rs.wasNull()) {
            Imagen img = new Imagen();
            img.setId(idImagen);
            img.setUrl(rs.getString("imagen_url"));
            snapshot.setImagen(img);
        }

        return snapshot;
    }

    private void prepararDeclaracion(PreparedStatement ps, RecetaSnapshot snapshot) throws SQLException {
        if (snapshot.getRecetaOriginal() != null && snapshot.getRecetaOriginal().getId() != null) {
            ps.setInt(1, snapshot.getRecetaOriginal().getId());
        } else {
            ps.setNull(1, Types.INTEGER);
        }

        ps.setString(2, snapshot.getNombre());
        ps.setDouble(3, snapshot.getPrecioHistorico());
        ps.setDouble(4, snapshot.getPrecioFinalHistorico());

        if (snapshot.getImagen() != null && snapshot.getImagen().getId() != null) {
            ps.setInt(5, snapshot.getImagen().getId());
        } else {
            ps.setNull(5, Types.INTEGER);
        }
    }
}