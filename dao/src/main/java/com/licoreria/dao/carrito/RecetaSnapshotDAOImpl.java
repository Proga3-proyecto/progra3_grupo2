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
import java.util.List;

public class RecetaSnapshotDAOImpl implements RecetaSnapshotDAO {

    @Override
    public RecetaSnapshot get(Connection con, Integer id) throws SQLException {
        final String sql = "SELECT id_receta_snapshot, id_receta_original, nombre, descripcion, " +
                "instrucciones, precio_historico, precio_final_historico " +
                "FROM Receta_Snapshot WHERE id_receta_snapshot = ?";

        RecetaSnapshot snapshot = DAOUtils.get(sql, con, (ps) -> ps.setInt(1, id), this::mapearSnapshot);

        if (snapshot != null) {
            cargarImagenes(con, snapshot);
            cargarElementos(con, snapshot);
        }
        return snapshot;
    }

    @Override
    public List<RecetaSnapshot> getAll(Connection con) throws SQLException {
        final String sql = "SELECT id_receta_snapshot, id_receta_original, nombre, descripcion, " +
                "instrucciones, precio_historico, precio_final_historico " +
                "FROM Receta_Snapshot";

        List<RecetaSnapshot> snapshots = DAOUtils.getAll(sql, con, this::mapearSnapshot);

        for (RecetaSnapshot snapshot : snapshots) {
            cargarImagenes(con, snapshot);
            cargarElementos(con, snapshot);
        }
        return snapshots;
    }

    @Override
    public RecetaSnapshot save(Connection con, RecetaSnapshot snapshot) throws SQLException {
        final String sql = "INSERT INTO Receta_Snapshot (id_receta_original, nombre, descripcion, " +
                "instrucciones, precio_historico, precio_final_historico) VALUES (?, ?, ?, ?, ?, ?)";

        DAOUtils.save(sql, con, (ps) -> prepararDeclaracion(ps, snapshot), (rs) -> snapshot.setId(rs.getInt(1)));

        if (snapshot.getId() != null) {
            guardarImagenes(con, snapshot);
            guardarElementos(con, snapshot);
        }
        return snapshot;
    }

    @Override
    public RecetaSnapshot update(Connection con, RecetaSnapshot snapshot) throws SQLException {
        final String sql = "UPDATE Receta_Snapshot SET id_receta_original = ?, nombre = ?, descripcion = ?, " +
                "instrucciones = ?, precio_historico = ?, precio_final_historico = ? " +
                "WHERE id_receta_snapshot = ?";

        DAOUtils.update(sql, con, (ps) -> {
            prepararDeclaracion(ps, snapshot);
            ps.setInt(7, snapshot.getId());
        });

        eliminarImagenes(con, snapshot.getId());
        guardarImagenes(con, snapshot);

        eliminarElementos(con, snapshot.getId());
        guardarElementos(con, snapshot);

        return snapshot;
    }

    @Override
    public void remove(Connection con, RecetaSnapshot snapshot) throws SQLException {
        eliminarImagenes(con, snapshot.getId());
        eliminarElementos(con, snapshot.getId());

        final String sql = "DELETE FROM Receta_Snapshot WHERE id_receta_snapshot = ?";
        DAOUtils.delete(sql, con, (ps) -> ps.setInt(1, snapshot.getId()));
    }

    private void cargarImagenes(Connection con, RecetaSnapshot snapshot) throws SQLException {
        final String sql = "SELECT i.id_imagen, i.url FROM Imagen i " +
                "INNER JOIN Receta_Snapshot_Imagen rsi ON i.id_imagen = rsi.id_imagen " +
                "WHERE rsi.id_receta_snapshot = ?";

        List<Imagen> imagenes = DAOUtils.getAll(sql, con,
                (ps) -> ps.setInt(1, snapshot.getId()),
                (rs) -> {
                    Imagen img = new Imagen();
                    img.setId(rs.getInt("id_imagen"));
                    img.setUrl(rs.getString("url"));
                    return img;
                }
        );
        snapshot.setImagenesHistoricas(imagenes);
    }

    private void guardarImagenes(Connection con, RecetaSnapshot snapshot) throws SQLException {
        if (snapshot.getImagenesHistoricas() == null || snapshot.getImagenesHistoricas().isEmpty()) return;

        final String sql = "INSERT INTO Receta_Snapshot_Imagen (id_receta_snapshot, id_imagen, principal) VALUES (?, ?, ?)";
        boolean isFirst = true; // El primer elemento insertado se marca como principal

        for (Imagen img : snapshot.getImagenesHistoricas()) {
            final boolean principal = isFirst;
            isFirst = false;

            DAOUtils.save(sql, con, (ps) -> {
                ps.setInt(1, snapshot.getId());
                ps.setInt(2, img.getId());
                ps.setBoolean(3, principal);
            }, (rs) -> {});
        }
    }

    private void eliminarImagenes(Connection con, Integer idSnapshot) throws SQLException {
        final String sql = "DELETE FROM Receta_Snapshot_Imagen WHERE id_receta_snapshot = ?";
        DAOUtils.delete(sql, con, (ps) -> ps.setInt(1, idSnapshot));
    }

    private void cargarElementos(Connection con, RecetaSnapshot snapshot) throws SQLException {
        final String sql = "SELECT id_producto_snapshot, cantidad FROM Receta_Snapshot_Elemento WHERE id_receta_snapshot = ?";

        List<RecetaSnapshotElemento> elementos = DAOUtils.getAll(sql, con,
                (ps) -> ps.setInt(1, snapshot.getId()),
                (rs) -> {
                    RecetaSnapshotElemento elemento = new RecetaSnapshotElemento();
                    elemento.setRecetaSnapshot(snapshot);
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

        final String sql = "INSERT INTO Receta_Snapshot_Elemento (id_receta_snapshot, id_producto_snapshot, cantidad) VALUES (?, ?, ?)";

        for (RecetaSnapshotElemento elemento : snapshot.getElementosHistoricos()) {
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
        snapshot.setDescripcion(rs.getString("descripcion"));
        snapshot.setInstrucciones(rs.getString("instrucciones"));
        snapshot.setPrecioHistorico(rs.getDouble("precio_historico"));
        snapshot.setPrecioFinalHistorico(rs.getDouble("precio_final_historico"));

        return snapshot;
    }

    private void prepararDeclaracion(PreparedStatement ps, RecetaSnapshot snapshot) throws SQLException {
        if (snapshot.getRecetaOriginal() != null && snapshot.getRecetaOriginal().getId() != null) {
            ps.setInt(1, snapshot.getRecetaOriginal().getId());
        } else {
            ps.setNull(1, Types.INTEGER);
        }

        ps.setString(2, snapshot.getNombre());
        ps.setString(3, snapshot.getDescripcion());
        ps.setString(4, snapshot.getInstrucciones());
        ps.setDouble(5, snapshot.getPrecioHistorico());
        ps.setDouble(6, snapshot.getPrecioFinalHistorico());
    }
}