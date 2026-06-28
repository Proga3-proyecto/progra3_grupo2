package com.licoreria.dao.catalogo;

import com.licoreria.dao.DAOUtils;
import com.licoreria.dominio.catalogo.*;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductoDAOImpl implements ProductoDAO {
    @Override
    public Producto get(Connection con, Integer id) throws SQLException {
        final String sql = "SELECT id_producto, nombre,descripcion, precio, precio_final, stock, descuento, volumen_litros, " +
                "porcentaje_alcohol, id_impuesto, id_impuesto_alcohol, id_marca " +
                "FROM Producto WHERE id_producto = ?";

        return DAOUtils.get(sql, con, (ps) -> ps.setInt(1, id), this::mapProduct);
    }

    @Override
    public List<Producto> getAll(Connection con) throws SQLException {
        final String sql = "SELECT id_producto, nombre,descripcion, precio, precio_final, stock, descuento, volumen_litros, " +
                "porcentaje_alcohol, id_impuesto, id_impuesto_alcohol, id_marca " +
                "FROM Producto";

        return DAOUtils.getAll(sql, con, this::mapProduct);
    }

    @Override
    public Producto save(Connection con, Producto producto) throws SQLException {
        final String sql = "INSERT INTO Producto (nombre,descripcion, precio, precio_final, stock, descuento, volumen_litros, " +
                "porcentaje_alcohol, id_impuesto, id_impuesto_alcohol, id_marca) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        DAOUtils.save(sql, con, (ps) -> {
            prepararDeclaracion(ps, producto);
        }, (rs) -> {
            producto.setId(rs.getInt(1));
        });

        return producto;
    }

    @Override
    public Producto update(Connection con, Producto producto) throws SQLException {
        final String sql = "UPDATE Producto SET nombre = ?,descripcion = ?, precio = ?, precio_final = ?, stock = ?, descuento = ?, " +
                "volumen_litros = ?, porcentaje_alcohol = ?, id_impuesto = ?, id_impuesto_alcohol = ?, " +
                "id_marca = ? WHERE id_producto = ?";

        DAOUtils.update(sql, con, (ps) -> {
            prepararDeclaracion(ps, producto);
            ps.setInt(12, producto.getId());
        });

        return producto;
    }

    @Override
    public void remove(Connection con, Producto producto) throws SQLException {
        final String sql = "DELETE FROM Producto WHERE id_producto = ?";
        DAOUtils.delete(sql, con, (ps) -> {
            ps.setInt(1, producto.getId());
        });
    }

    private Producto mapProduct(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setId(rs.getInt("id_producto"));
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setPrecio(rs.getDouble("precio"));
        producto.setPrecioFinal(rs.getDouble("precio_final"));
        producto.setStock(rs.getInt("stock"));
        producto.setDescuento(rs.getDouble("descuento"));
        producto.setVolumenLitros(rs.getDouble("volumen_litros"));
        producto.setPorcentajeAlcohol(rs.getDouble("porcentaje_alcohol"));

        Marca marca = new Marca();
        marca.setId(rs.getInt("id_marca"));
        producto.setMarca(marca);

        int idImpuesto = rs.getInt("id_impuesto");
        if (!rs.wasNull()) {
            Impuesto impuestoBase = new Impuesto();
            impuestoBase.setId(idImpuesto);
            producto.setImpuestoBase(impuestoBase);
        }

        int idImpuestoAlcohol = rs.getInt("id_impuesto_alcohol");
        if (!rs.wasNull()) {
            AlcoholImpuesto alcoholImpuesto = new AlcoholImpuesto();
            alcoholImpuesto.setId(idImpuestoAlcohol);
            producto.setImpuestoAlcohol(alcoholImpuesto);
        }
        return producto;
    }

    private void prepararDeclaracion(PreparedStatement ps, Producto producto) throws SQLException {
        ps.setString(1, producto.getNombre());
        ps.setString(2, producto.getDescripcion());
        ps.setDouble(3, producto.getPrecio());
        ps.setDouble(4, producto.getPrecioFinal());
        ps.setInt(5, producto.getStock());
        ps.setDouble(6, producto.getDescuento());
        ps.setDouble(7, producto.getVolumenLitros());
        ps.setDouble(8, producto.getPorcentajeAlcohol());

        if (producto.getImpuestoBase() != null && producto.getImpuestoBase().getId() != null) {
            ps.setInt(9, producto.getImpuestoBase().getId());
        } else {
            ps.setNull(9, Types.INTEGER);
        }

        if (producto.getImpuestoAlcohol() != null && producto.getImpuestoAlcohol().getId() != null) {
            ps.setInt(10, producto.getImpuestoAlcohol().getId());
        } else {
            ps.setNull(10, Types.INTEGER);
        }

        if (producto.getMarca() != null && producto.getMarca().getId() != null) {
            ps.setInt(11, producto.getMarca().getId());
        } else {
            ps.setNull(11, Types.INTEGER);
        }
    }

    @Override
    public void cargarImagen(Connection con, Producto producto, Imagen imagen) throws SQLException {
        final String sql = "INSERT INTO ProductoImagen (id_producto, id_imagen, principal) VALUES (?, ?, ?)";
        DAOUtils.update(sql, con, (ps) -> {
            ps.setInt(1, producto.getId());
            ps.setInt(2, imagen.getId());
            ps.setBoolean(3, imagen.isPrincipal());
        });
    }

    @Override
    public void removerImagen(Connection con, Producto producto, Imagen imagen) throws SQLException {
        final String sql = "DELETE FROM ProductoImagen WHERE id_producto = ? AND id_imagen = ?";
        DAOUtils.delete(sql, con, (ps) -> {
            ps.setInt(1, producto.getId());
            ps.setInt(2, imagen.getId());
        });
    }

    @Override
    public void removerImagen(Connection con, int idProducto, int idImagen) throws SQLException {
        final String sql = "DELETE FROM ProductoImagen WHERE id_producto = ? AND id_imagen = ?";
        DAOUtils.delete(sql, con, (ps) -> {
            ps.setInt(1, idProducto);
            ps.setInt(2, idImagen);
        });
    }

    @Override
    public void asignarImagenPrincipal(Connection con, Producto producto, Imagen imagen) throws SQLException {
        final String resetSql = "UPDATE ProductoImagen SET principal = false WHERE id_producto = ?";
        DAOUtils.update(resetSql, con, (ps) -> {
            ps.setInt(1, producto.getId());
        });
        final String setSql = "UPDATE ProductoImagen SET principal = true WHERE id_producto = ? AND id_imagen = ?";
        DAOUtils.update(setSql, con, (ps) -> {
            ps.setInt(1, producto.getId());
            ps.setInt(2, imagen.getId());
        });
    }

    @Override
    public void asignarCategoria(Connection con, Producto producto, Categoria categoria) throws SQLException {
        final String sql = "INSERT INTO Producto_Categoria (id_producto, id_categoria) VALUES (?, ?)";
        DAOUtils.update(sql, con, (ps) -> {
            ps.setInt(1, producto.getId());
            ps.setInt(2, categoria.getId());
        });
    }

    @Override
    public void removerCategoria(Connection con, Producto producto, Categoria categoria) throws SQLException {
        final String sql = "DELETE FROM Producto_Categoria WHERE id_producto = ? AND id_categoria = ?";
        DAOUtils.delete(sql, con, (ps) -> {
            ps.setInt(1, producto.getId());
            ps.setInt(2, categoria.getId());
        });
    }

    @Override
    public List<Producto> getProductosPorCliente(Connection con, int idCliente) throws SQLException {
        final String sql = "SELECT p.id_producto,p.descripcion, p.nombre, p.precio, p.precio_final, p.stock, " +
                "p.descuento, p.volumen_litros, p.porcentaje_alcohol, p.id_impuesto, " +
                "p.id_impuesto_alcohol, p.id_marca " +
                "FROM Producto p " +
                "INNER JOIN Detalle_Producto dp ON p.id_producto = dp.id_producto " +
                "WHERE dp.id_cliente_carrito = ?";

        return DAOUtils.getAll(sql, con, (ps) -> ps.setInt(1, idCliente), this::mapProduct);
    }

    @Override
    public Map<Integer, Producto> getMapByIds(Connection con, List<Integer> idsProductos) throws SQLException {

        Map<Integer, Producto> resultado = new HashMap<>();

        if (idsProductos == null || idsProductos.isEmpty()) {
            return resultado;
        }

        String placeholders = idsProductos.stream()
                .map(id -> "?")
                .collect(Collectors.joining(","));

        String sql = "SELECT id_producto, nombre, descripcion, precio, precio_final, stock, descuento, volumen_litros, " +
                "porcentaje_alcohol, id_impuesto, id_impuesto_alcohol, id_marca " +
                "FROM Producto WHERE id_producto IN (" + placeholders + ")";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            int index = 1;
            for (Integer id : idsProductos) {
                ps.setInt(index++, id);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto producto = mapProduct(rs);
                    resultado.put(producto.getId(), producto);
                }
            }
        }

        return resultado;
    }
}