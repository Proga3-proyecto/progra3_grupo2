package com.licoreria.dao.catalogo;

import com.licoreria.dao.DAOUtils;
import com.licoreria.dominio.catalogo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecetaDAOImpl implements RecetaDAO {

    @Override
    public Receta get(Connection con, Integer id) throws SQLException {
        final String sql = "SELECT id_receta, nombre, descripcion, instrucciones, descuento, precio, precio_final " +
                "FROM Receta WHERE id_receta = ?";

        Receta receta = DAOUtils.get(sql, con, (ps) -> ps.setInt(1, id), this::mapearReceta);

        if (receta != null) {
            cargarElementos(con, receta);
        }

        return receta;
    }

    @Override
    public List<Receta> getAll(Connection con) throws SQLException {
        final String sql = "SELECT id_receta, nombre, descripcion, instrucciones, descuento, precio, precio_final " +
                "FROM Receta";

        List<Receta> recetas = DAOUtils.getAll(sql, con, this::mapearReceta);

        if (recetas == null || recetas.isEmpty()) {
            return recetas;
        }

        List<Integer> idsRecetas = recetas.stream().map(Receta::getId).toList();
        Map<Integer, List<ElementoReceta>> mapaElementos = getAllElementosByRecetas(con, idsRecetas);
        for (Receta receta : recetas) {
            receta.setElementos(mapaElementos.getOrDefault(receta.getId(), new java.util.ArrayList<>()));
        }

        return recetas;
    }

    @Override
    public Receta save(Connection con, Receta receta) throws SQLException {
        final String sql = "INSERT INTO Receta (nombre, descripcion, instrucciones, descuento, precio, precio_final) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        DAOUtils.save(sql, con, (ps) -> {
            prepararDeclaracion(ps, receta);
        }, (rs) -> {
            receta.setId(rs.getInt(1));
        });

        if (receta.getId() != null && receta.getElementos() != null && !receta.getElementos().isEmpty()) {
            guardarElementos(con, receta);
        }

        return receta;
    }

    @Override
    public Receta update(Connection con, Receta receta) throws SQLException {
        final String sql = "UPDATE Receta SET nombre = ?, descripcion = ?, instrucciones = ?, descuento = ?, " +
                "precio = ?, precio_final = ? WHERE id_receta = ?";

        DAOUtils.update(sql, con, (ps) -> {
            prepararDeclaracion(ps, receta);
            ps.setInt(7, receta.getId());
        });

        eliminarElementos(con, receta.getId());
        if (receta.getElementos() != null && !receta.getElementos().isEmpty()) {
            guardarElementos(con, receta);
        }

        return receta;
    }

    @Override
    public void remove(Connection con, Receta receta) throws SQLException {
        eliminarElementos(con, receta.getId());

        final String sql = "DELETE FROM Receta WHERE id_receta = ?";
        DAOUtils.delete(sql, con, (ps) -> {
            ps.setInt(1, receta.getId());
        });
    }

    private void cargarElementos(Connection con, Receta receta) throws SQLException {
        final String sqlElemento = "SELECT id_elemento_receta, id_producto, cantidad FROM Elemento_Receta WHERE id_receta = ?";
        List<ElementoReceta> elementos = DAOUtils.getAll(sqlElemento, con,
                (ps) -> ps.setInt(1, receta.getId()),
                (rs) -> {
                    ElementoReceta ele = new ElementoReceta();
                    ele.setId(rs.getInt("id_elemento_receta"));
                    ele.setCantidad(rs.getDouble("cantidad"));
                    int idProducto = rs.getInt("id_producto");
                    Producto producto = new Producto();
                    producto.setId(idProducto);
                    ele.setProducto(producto);
                    return ele;
                }
        );
        receta.setElementos(elementos);
    }

    private Map<Integer, List<ElementoReceta>> getAllElementosByRecetas(Connection con, List<Integer> idRecetas) throws SQLException {
        Map<Integer, List<ElementoReceta>> resultado = new java.util.HashMap<>();

        if (idRecetas == null || idRecetas.isEmpty()) {
            return resultado;
        }

        String placeholders = idRecetas.stream()
                .map(id -> "?")
                .collect(java.util.stream.Collectors.joining(","));

        // Seleccionamos id_receta para saber a quién pertenece cada elemento
        String sql = "SELECT id_receta, id_elemento_receta, id_producto, cantidad " +
                "FROM Elemento_Receta WHERE id_receta IN (" + placeholders + ")";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            int index = 1;
            for (Integer id : idRecetas) {
                ps.setInt(index++, id);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Integer idReceta = rs.getInt("id_receta");

                    ElementoReceta ele = new ElementoReceta();
                    ele.setId(rs.getInt("id_elemento_receta"));
                    ele.setCantidad(rs.getDouble("cantidad"));

                    Producto producto = new Producto();
                    producto.setId(rs.getInt("id_producto"));
                    ele.setProducto(producto);

                    // Agrupamos el elemento en la lista correspondiente a su id_receta
                    resultado.computeIfAbsent(idReceta, k -> new java.util.ArrayList<>()).add(ele);
                }
            }
        }
        return resultado;
    }

    private void guardarElementos(Connection con, Receta receta) throws SQLException {
        final String sqlElemento = "INSERT INTO Elemento_Receta (id_receta, id_producto, cantidad) VALUES (?, ?, ?)";
        for (ElementoReceta ele : receta.getElementos()) {
            DAOUtils.save(sqlElemento, con, (ps) -> {
                ps.setInt(1, receta.getId());
                ps.setInt(2, ele.getProducto().getId());
                ps.setDouble(3, ele.getCantidad());
            }, (rs) -> {
                ele.setId(rs.getInt(1));
            });
        }
    }

    private void eliminarElementos(Connection con, Integer idReceta) throws SQLException {
        final String sql = "DELETE FROM Elemento_Receta WHERE id_receta = ?";
        DAOUtils.delete(sql, con, (ps) -> ps.setInt(1, idReceta));
    }

    private Receta mapearReceta(ResultSet rs) throws SQLException {
        Receta receta = new Receta();
        receta.setId(rs.getInt("id_receta"));
        receta.setNombre(rs.getString("nombre"));
        receta.setDescripcion(rs.getString("descripcion"));
        receta.setInstrucciones(rs.getString("instrucciones"));
        receta.setDescuento(rs.getDouble("descuento"));
        receta.setPrecio(rs.getDouble("precio"));
        receta.setPrecioFinal(rs.getDouble("precio_final"));
        return receta;
    }

    private void prepararDeclaracion(PreparedStatement ps, Receta receta) throws SQLException {
        ps.setString(1, receta.getNombre());
        ps.setString(2, receta.getDescripcion());
        ps.setString(3, receta.getInstrucciones());
        ps.setDouble(4, receta.getDescuento());
        ps.setDouble(5, receta.getPrecio());
        ps.setDouble(6, receta.getPrecioFinal());
    }

    @Override
    public void cargarImagen(Connection con, Receta receta, Imagen imagen) throws SQLException {
        final String sql = "INSERT INTO RecetaImagen (id_receta, id_imagen, principal) VALUES (?, ?, ?)";
        DAOUtils.save(sql, con, (ps) -> {
            ps.setInt(1, receta.getId());
            ps.setInt(2, imagen.getId());
            ps.setBoolean(3, false); // No es principal por defecto
        }, (rs) -> {
        });
    }

    @Override
    public void asignarImagenPrincipal(Connection con, Receta receta, Imagen imagen) throws SQLException {
        // Primero, quitar la anterior principal
        final String sqlUpdate = "UPDATE RecetaImagen SET principal = false WHERE id_receta = ?";
        DAOUtils.update(sqlUpdate, con, (ps) -> ps.setInt(1, receta.getId()));

        // Luego insertar o actualizar la nueva principal
        final String sqlInsert = "INSERT INTO RecetaImagen (id_receta, id_imagen, principal) VALUES (?, ?, true) " +
                "ON DUPLICATE KEY UPDATE principal = true";
        DAOUtils.save(sqlInsert, con, (ps) -> {
            ps.setInt(1, receta.getId());
            ps.setInt(2, imagen.getId());
        }, (rs) -> {
        });
    }

    @Override
    public void removerImagen(Connection con, int idReceta, int idImagen) throws SQLException {
        final String sql = "DELETE FROM RecetaImagen WHERE id_receta = ? AND id_imagen = ?";
        DAOUtils.delete(sql, con, (ps) -> {
            ps.setInt(1, idReceta);
            ps.setInt(2, idImagen);
        });
    }

    // En RecetaDAOImpl.java
    @Override
    public void asignarCategoria(Connection con, Receta receta, Categoria categoria) throws SQLException {
        final String sql = "INSERT INTO Receta_Categoria (id_receta, id_categoria) VALUES (?, ?)";
        DAOUtils.update(sql, con, (ps) -> {
            ps.setInt(1, receta.getId());
            ps.setInt(2, categoria.getId());
        });
    }

    @Override
    public void removerCategoria(Connection con, Receta receta, Categoria categoria) throws SQLException {
        final String sql = "DELETE FROM Receta_Categoria WHERE id_receta = ? AND id_categoria = ?";
        DAOUtils.delete(sql, con, (ps) -> {
            ps.setInt(1, receta.getId());
            ps.setInt(2, categoria.getId());
        });
    }

    @Override
    public List<Receta> getRecetasPorCliente(Connection con, int idCliente) throws SQLException {
        final String sql = "SELECT r.id_receta, r.nombre, r.descripcion, r.instrucciones, r.descuento, r.precio, r.precio_final " +
                "FROM Receta r " +
                "INNER JOIN Detalle_Receta dr ON r.id_receta = dr.id_receta " +
                "WHERE dr.id_cliente_carrito = ?";
        List<Receta> recetas = DAOUtils.getAll(sql, con,
                (ps) -> ps.setInt(1, idCliente),
                this::mapearReceta
        );
        if (recetas == null || recetas.isEmpty()) {
            return recetas;
        }
        List<Integer> idsRecetas = recetas.stream().map(Receta::getId).toList();
        Map<Integer, List<ElementoReceta>> mapaElementos = getAllElementosByRecetas(con, idsRecetas);
        for (Receta receta : recetas) {
            receta.setElementos(mapaElementos.getOrDefault(receta.getId(), new java.util.ArrayList<>()));
        }
        return recetas;
    }


    @Override
    public Map<Integer, Receta> getMapByIds(Connection con, List<Integer> idsRecetas) throws SQLException {

        Map<Integer, Receta> resultado = new HashMap<>();
        List<Receta> recetas = new ArrayList<>();

        if (idsRecetas == null || idsRecetas.isEmpty()) {
            return resultado;
        }

        String placeholders = idsRecetas.stream()
                .map(id -> "?")
                .collect(Collectors.joining(","));

        final String sql = "SELECT id_receta, nombre, descripcion, instrucciones, descuento, precio, precio_final " +
                "FROM Receta WHERE id_receta  IN (" + placeholders +  ")";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            int index = 1;
            for (Integer id : idsRecetas) {
                ps.setInt(index++, id);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Receta receta = mapearReceta(rs);
                    recetas.add(receta);
                    resultado.put(receta.getId(), receta);
                }
            }
        }

        Map<Integer, List<ElementoReceta>> mapaElementos = getAllElementosByRecetas(con, idsRecetas);
        for (Receta receta : recetas) {
            receta.setElementos(mapaElementos.getOrDefault(receta.getId(), new java.util.ArrayList<>()));
        }
        return resultado;
    }

}