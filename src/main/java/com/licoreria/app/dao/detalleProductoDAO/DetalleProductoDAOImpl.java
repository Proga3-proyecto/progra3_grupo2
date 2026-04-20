package com.licoreria.app.dao.detalleProductoDAO;

import com.licoreria.app.dao.conexionBD.ConexionDB;
import com.licoreria.app.modelo.pedidos.DetalleProducto;
import com.licoreria.app.modelo.productos.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleProductoDAOImpl implements DetalleProductoDAO {

    @Override
    public List<DetalleProducto> getByCarrito(long idCliente) {
        return obtenerDetalles("SELECT * FROM Detalle_Producto WHERE id_cliente_carrito = ?", idCliente);
    }

    @Override
    public List<DetalleProducto> getByPedido(long idPedido) {
        return obtenerDetalles("SELECT * FROM Detalle_Producto WHERE id_pedido = ?", idPedido);
    }

    @Override
    public void save(DetalleProducto detalle, Long idPedido, Long idClienteCarrito) {
        String query = "INSERT INTO Detalle_Producto (id_pedido, id_cliente_carrito, id_producto, cantidad, descuento_total, monto_total) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            setNullableParams(ps, idPedido, idClienteCarrito);
            ps.setLong(3, detalle.getProducto().getId());
            ps.setInt(4, detalle.getCantidad());
            ps.setDouble(5, detalle.getDescuentoTotal());
            ps.setDouble(6, detalle.getMontoTotal());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    detalle.setId(rs.getLong(1)); // Requiere agregar idDetalle a DetallePedido
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(DetalleProducto detalle, Long idPedido, Long idClienteCarrito) {
        String query = "UPDATE Detalle_Producto SET id_pedido=?, id_cliente_carrito=?, id_producto=?, cantidad=?, descuento_total=?, monto_total=? WHERE id_detalle_producto=?";

        try (Connection conn = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = conn.prepareStatement(query)) {

            setNullableParams(ps, idPedido, idClienteCarrito);
            ps.setLong(3, detalle.getProducto().getId());
            ps.setInt(4, detalle.getCantidad());
            ps.setDouble(5, detalle.getDescuentoTotal());
            ps.setDouble(6, detalle.getMontoTotal());
            ps.setLong(7, detalle.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long idDetalleProducto) {
        String query = "DELETE FROM Detalle_Producto WHERE id_detalle_producto = ?";
        try (Connection conn = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, idDetalleProducto);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private List<DetalleProducto> obtenerDetalles(String query, long idFiltro) {
        List<DetalleProducto> lista = new ArrayList<>();
        try (Connection conn = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, idFiltro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setId(rs.getLong("id_producto"));

                    DetalleProducto dp = new DetalleProducto(p, rs.getInt("cantidad"));
                    dp.setId(rs.getLong("id_detalle_producto"));
                    dp.setDescuentoTotal(rs.getDouble("descuento_total"));
                    dp.setMontoTotal(rs.getDouble("monto_total"));
                    lista.add(dp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private void setNullableParams(PreparedStatement ps, Long idPedido, Long idClienteCarrito) throws SQLException {
        if (idPedido != null) ps.setLong(1, idPedido);
        else ps.setNull(1, java.sql.Types.INTEGER);

        if (idClienteCarrito != null) ps.setLong(2, idClienteCarrito);
        else ps.setNull(2, java.sql.Types.INTEGER);
    }
}