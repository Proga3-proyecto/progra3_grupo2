package com.licoreria.dao.carrito;

import com.licoreria.dao.DAOUtils;
import com.licoreria.dominio.Snapshots.ProductoSnapshot;
import com.licoreria.dominio.catalogo.Imagen;
import com.licoreria.dominio.catalogo.Producto;
import com.licoreria.dominio.catalogo.TipoImpuesto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductoSnapshotDAOImpl implements ProductoSnapshotDAO {

    @Override
    public ProductoSnapshot get(Connection con, Integer id) throws SQLException {
        final String sql = "SELECT ps.id_producto_snapshot, ps.id_producto_original, ps.nombre, ps.precio_venta, " +
                "ps.precio_final_venta, ps.descuento_applied, ps.volumen_litros, ps.porcentaje_alcohol, ps.nombre_marca, " +
                "ps.nombre_impuesto, ps.porcentaje_impuesto, ps.tipo_impuesto, " +
                "ps.valor_impuesto_alcohol_historico, ps.id_imagen, i.url AS imagen_url " +
                "FROM Producto_Snapshot ps " +
                "LEFT JOIN Imagen i ON ps.id_imagen = i.id_imagen " +
                "WHERE ps.id_producto_snapshot = ?";

        ProductoSnapshot snapshot = DAOUtils.get(sql, con, (ps) -> ps.setInt(1, id), this::mapearSnapshot);

        if (snapshot != null) {
            cargarCategorias(con, snapshot);
        }
        return snapshot;
    }

    @Override
    public List<ProductoSnapshot> getAll(Connection con) throws SQLException {
        final String sql = "SELECT ps.id_producto_snapshot, ps.id_producto_original, ps.nombre, ps.precio_venta, " +
                "ps.precio_final_venta, ps.descuento_applied, ps.volumen_litros, ps.porcentaje_alcohol, ps.nombre_marca, " +
                "ps.nombre_impuesto, ps.porcentaje_impuesto, ps.tipo_impuesto, " +
                "ps.valor_impuesto_alcohol_historico, ps.id_imagen, i.url AS imagen_url " +
                "FROM Producto_Snapshot ps " +
                "LEFT JOIN Imagen i ON ps.id_imagen = i.id_imagen";

        List<ProductoSnapshot> snapshots = DAOUtils.getAll(sql, con, this::mapearSnapshot);

        for (ProductoSnapshot snapshot : snapshots) {
            cargarCategorias(con, snapshot);
        }
        return snapshots;
    }

    @Override
    public ProductoSnapshot save(Connection con, ProductoSnapshot snapshot) throws SQLException {
        final String sql = "INSERT INTO Producto_Snapshot (id_producto_original, nombre, precio_venta, " +
                "precio_final_venta, descuento_applied, volumen_litros, porcentaje_alcohol, nombre_marca, " +
                "nombre_impuesto, porcentaje_impuesto, tipo_impuesto, " +
                "valor_impuesto_alcohol_historico, id_imagen) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        DAOUtils.save(sql, con, (ps) -> prepararDeclaracion(ps, snapshot), (rs) -> snapshot.setId(rs.getInt(1)));

        if (snapshot.getId() != null) {
            guardarCategorias(con, snapshot);
        }
        return snapshot;
    }

    @Override
    public ProductoSnapshot update(Connection con, ProductoSnapshot snapshot) throws SQLException {
        final String sql = "UPDATE Producto_Snapshot SET id_producto_original = ?, nombre = ?, precio_venta = ?, " +
                "precio_final_venta = ?, descuento_applied = ?, volumen_litros = ?, porcentaje_alcohol = ?, " +
                "nombre_marca = ?, nombre_impuesto = ?, porcentaje_impuesto = ?, tipo_impuesto = ?, " +
                "valor_impuesto_alcohol_historico = ?, id_imagen = ? " +
                "WHERE id_producto_snapshot = ?";

        DAOUtils.update(sql, con, (ps) -> {
            prepararDeclaracion(ps, snapshot);
            ps.setInt(14, snapshot.getId());
        });

        eliminarCategorias(con, snapshot.getId());
        guardarCategorias(con, snapshot);

        return snapshot;
    }

    @Override
    public void remove(Connection con, ProductoSnapshot snapshot) throws SQLException {
        eliminarCategorias(con, snapshot.getId());

        final String sql = "DELETE FROM Producto_Snapshot WHERE id_producto_snapshot = ?";
        DAOUtils.delete(sql, con, (ps) -> ps.setInt(1, snapshot.getId()));
    }

    @Override
    public Map<Integer, ProductoSnapshot> getMapByIds(Connection con, List<Integer> idsSnapshots) throws SQLException {
        Map<Integer, ProductoSnapshot> resultado = new HashMap<>();
        if (idsSnapshots == null || idsSnapshots.isEmpty()) return resultado;

        String placeholders = idsSnapshots.stream().map(id -> "?").collect(Collectors.joining(","));
        String sql = "SELECT ps.id_producto_snapshot, ps.id_producto_original, ps.nombre, ps.precio_venta, " +
                "ps.precio_final_venta, ps.descuento_applied, ps.volumen_litros, ps.porcentaje_alcohol, ps.nombre_marca, " +
                "ps.nombre_impuesto, ps.porcentaje_impuesto, ps.tipo_impuesto, " +
                "ps.valor_impuesto_alcohol_historico, ps.id_imagen, i.url AS imagen_url " +
                "FROM Producto_Snapshot ps " +
                "LEFT JOIN Imagen i ON ps.id_imagen = i.id_imagen " +
                "WHERE ps.id_producto_snapshot IN (" + placeholders + ")";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            int index = 1;
            for (Integer id : idsSnapshots) {
                ps.setInt(index++, id);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductoSnapshot snapshot = mapearSnapshot(rs);
                    resultado.put(snapshot.getId(), snapshot);
                }
            }
        }

        for (ProductoSnapshot snapshot : resultado.values()) {
            cargarCategorias(con, snapshot);
        }

        return resultado;
    }

    private void cargarCategorias(Connection con, ProductoSnapshot snapshot) throws SQLException {
        final String sql = "SELECT nombre_categoria FROM Producto_Snapshot_Categoria WHERE id_producto_snapshot = ?";
        List<String> categorias = DAOUtils.getAll(sql, con,
                (ps) -> ps.setInt(1, snapshot.getId()),
                (rs) -> rs.getString("nombre_categoria")
        );
        snapshot.setCategoriasHistoricas(categorias);
    }

    private void guardarCategorias(Connection con, ProductoSnapshot snapshot) throws SQLException {
        if (snapshot.getCategoriasHistoricas() == null || snapshot.getCategoriasHistoricas().isEmpty()) return;

        final String sql = "INSERT INTO Producto_Snapshot_Categoria (id_producto_snapshot, nombre_categoria) VALUES (?, ?)";
        for (String nombreCat : snapshot.getCategoriasHistoricas()) {
            DAOUtils.save(sql, con, (ps) -> {
                ps.setInt(1, snapshot.getId());
                ps.setString(2, nombreCat);
            }, (rs) -> {});
        }
    }

    private void eliminarCategorias(Connection con, Integer idSnapshot) throws SQLException {
        final String sql = "DELETE FROM Producto_Snapshot_Categoria WHERE id_producto_snapshot = ?";
        DAOUtils.delete(sql, con, (ps) -> ps.setInt(1, idSnapshot));
    }


    private ProductoSnapshot mapearSnapshot(ResultSet rs) throws SQLException {
        ProductoSnapshot snapshot = new ProductoSnapshot();
        snapshot.setId(rs.getInt("id_producto_snapshot"));

        int idOriginal = rs.getInt("id_producto_original");
        if (!rs.wasNull()) {
            Producto p = new Producto();
            p.setId(idOriginal);
            snapshot.setProductoOriginal(p);
        }

        snapshot.setNombre(rs.getString("nombre"));
        snapshot.setPrecioVenta(rs.getDouble("precio_venta"));
        snapshot.setPrecioFinalVenta(rs.getDouble("precio_final_venta"));
        snapshot.setDescuentoApplied(rs.getDouble("descuento_applied"));
        snapshot.setVolumenLitros(rs.getDouble("volumen_litros"));
        snapshot.setPorcentajeAlcohol(rs.getDouble("porcentaje_alcohol"));
        snapshot.setNombreMarca(rs.getString("nombre_marca"));
        snapshot.setNombreImpuesto(rs.getString("nombre_impuesto"));
        snapshot.setPorcentajeImpuesto(rs.getDouble("porcentaje_impuesto"));
        snapshot.setTipoImpuesto(TipoImpuesto.valueOf(rs.getString("tipo_impuesto")));
        snapshot.setValorImpuestoAlcoholHistorico(rs.getDouble("valor_impuesto_alcohol_historico"));

        // Mapeo de la imagen si existe
        int idImagen = rs.getInt("id_imagen");
        if (!rs.wasNull()) {
            Imagen img = new Imagen();
            img.setId(idImagen);
            img.setUrl(rs.getString("imagen_url"));
            snapshot.setImagen(img);
        }

        return snapshot;
    }

    private void prepararDeclaracion(PreparedStatement ps, ProductoSnapshot snapshot) throws SQLException {
        if (snapshot.getProductoOriginal() != null && snapshot.getProductoOriginal().getId() != null) {
            ps.setInt(1, snapshot.getProductoOriginal().getId());
        } else {
            ps.setNull(1, Types.INTEGER);
        }

        ps.setString(2, snapshot.getNombre());
        ps.setDouble(3, snapshot.getPrecioVenta());
        ps.setDouble(4, snapshot.getPrecioFinalVenta());
        ps.setDouble(5, snapshot.getDescuentoApplied());
        ps.setDouble(6, snapshot.getVolumenLitros());
        ps.setDouble(7, snapshot.getPorcentajeAlcohol());
        ps.setString(8, snapshot.getNombreMarca());
        ps.setString(9, snapshot.getNombreImpuesto());
        ps.setDouble(10, snapshot.getPorcentajeImpuesto());
        ps.setString(11, snapshot.getTipoImpuesto().name());

        if (snapshot.getValorImpuestoAlcoholHistorico() != null) {
            ps.setDouble(12, snapshot.getValorImpuestoAlcoholHistorico());
        } else {
            ps.setDouble(12, 0.0);
        }

        if (snapshot.getImagen() != null && snapshot.getImagen().getId() != null) {
            ps.setInt(13, snapshot.getImagen().getId());
        } else {
            ps.setNull(13, Types.INTEGER);
        }
    }
}