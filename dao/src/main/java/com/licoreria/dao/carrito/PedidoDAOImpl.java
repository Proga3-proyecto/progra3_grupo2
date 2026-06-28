package com.licoreria.dao.carrito;

import com.licoreria.dao.DAOUtils;
import com.licoreria.dominio.Snapshots.ProductoSnapshot;
import com.licoreria.dominio.Snapshots.RecetaSnapshot;
import com.licoreria.dominio.carrito.Pedido;
import com.licoreria.dominio.carrito.PedidoDetalleProducto;
import com.licoreria.dominio.carrito.PedidoDetalleReceta;
import com.licoreria.dominio.carrito.EstadoPedido;
import com.licoreria.dominio.usuarios.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {

    @Override
    public Pedido get(Connection con, Integer id) throws SQLException {
        final String sql = "SELECT id_pedido, id_cliente, fecha_pedido, hora_inicio, hora_fin, " +
                "precio_total, total_impuestos, precio_delivery, " +
                "(precio_total + total_impuestos + precio_delivery) AS precio_final, estado, direccion_destino " +
                "FROM Pedido WHERE id_pedido = ?";

        Pedido pedido = DAOUtils.get(sql, con, (ps) -> ps.setInt(1, id), this::mapPedido);

        if (pedido != null) {
            loadDetails(con, pedido);
        }
        return pedido;
    }

    @Override
    public List<Pedido> getAll(Connection con) throws SQLException {
        final String sql = "SELECT id_pedido, id_cliente, fecha_pedido, hora_inicio, hora_fin, " +
                "precio_total, total_impuestos, precio_delivery, " +
                "(precio_total + total_impuestos + precio_delivery) AS precio_final, estado, direccion_destino " +
                "FROM Pedido";

        List<Pedido> pedidos = DAOUtils.getAll(sql, con, this::mapPedido);

        for (Pedido pedido : pedidos) {
            loadDetails(con, pedido);
        }
        return pedidos;
    }

    @Override
    public List<Pedido> getPedidosPorCliente(Connection con, Integer idCliente) throws SQLException {
        final String sql = "SELECT id_pedido, id_cliente, fecha_pedido, hora_inicio, hora_fin, " +
                "precio_total, total_impuestos, precio_delivery, " +
                "(precio_total + total_impuestos + precio_delivery) AS precio_final, estado, direccion_destino " +
                "FROM Pedido WHERE id_cliente = ?";

        List<Pedido> pedidos = DAOUtils.getAll(sql, con, (ps) -> ps.setInt(1, idCliente), this::mapPedido);

        for (Pedido pedido : pedidos) {
            loadDetails(con, pedido);
        }
        return pedidos;
    }

    @Override
    public Pedido save(Connection con, Pedido pedido) throws SQLException {
        final String sql = "INSERT INTO Pedido (id_cliente, fecha_pedido, hora_inicio, hora_fin, precio_total, " +
                "total_impuestos, precio_delivery, estado, direccion_destino) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        DAOUtils.save(sql, con, (ps) -> preparerDeclaration(ps, pedido), (rs) -> pedido.setId(rs.getInt(1)));

        if (pedido.getId() != null) {
            saveDetails(con, pedido);
        }
        return pedido;
    }

    @Override
    public Pedido update(Connection con, Pedido pedido) throws SQLException {
        final String sql = "UPDATE Pedido SET id_cliente = ?, fecha_pedido = ?, hora_inicio = ?, hora_fin = ?, " +
                "precio_total = ?, total_impuestos = ?, precio_delivery = ?, " +
                "estado = ?, direccion_destino = ? WHERE id_pedido = ?";

        DAOUtils.update(sql, con, (ps) -> {
            preparerDeclaration(ps, pedido);
            ps.setInt(10, pedido.getId());
        });

        deleteDetails(con, pedido.getId());
        saveDetails(con, pedido);

        return pedido;
    }

    @Override
    public void remove(Connection con, Pedido pedido) throws SQLException {
        deleteDetails(con, pedido.getId());

        final String sql = "DELETE FROM Pedido WHERE id_pedido = ?";
        DAOUtils.delete(sql, con, (ps) -> ps.setInt(1, pedido.getId()));
    }

    private void loadDetails(Connection con, Pedido pedido) throws SQLException {
        final String sqlProd = "SELECT id_pedido_detalle_prod, id_producto_snapshot, cantidad " +
                "FROM Pedido_Detalle_Producto WHERE id_pedido = ?";

        List<PedidoDetalleProducto> detProductos = DAOUtils.getAll(sqlProd, con,
                (ps) -> ps.setInt(1, pedido.getId()),
                (rs) -> {
                    PedidoDetalleProducto dp = new PedidoDetalleProducto();
                    dp.setId(rs.getInt("id_pedido_detalle_prod"));
                    // No seteamos el pedido para evitar recursion infinita en el JSON

                    ProductoSnapshot snapshot = new ProductoSnapshot();
                    snapshot.setId(rs.getInt("id_producto_snapshot"));
                    dp.setProductoSnapshot(snapshot);

                    dp.setCantidad(rs.getInt("cantidad"));
                    return dp;
                }
        );
        pedido.setDetallesProductos(detProductos);

        final String sqlRec = "SELECT id_pedido_detalle_receta, id_receta_snapshot, cantidad, descuento_historico " +
                "FROM Pedido_Detalle_Receta WHERE id_pedido = ?";

        List<PedidoDetalleReceta> detRecetas = DAOUtils.getAll(sqlRec, con,
                (ps) -> ps.setInt(1, pedido.getId()),
                (rs) -> {
                    PedidoDetalleReceta dr = new PedidoDetalleReceta();
                    dr.setId(rs.getInt("id_pedido_detalle_receta"));
                    // No seteamos el pedido para evitar recursion infinita en el JSON

                    RecetaSnapshot snapshot = new RecetaSnapshot();
                    snapshot.setId(rs.getInt("id_receta_snapshot"));
                    dr.setRecetaSnapshot(snapshot);

                    dr.setCantidad(rs.getInt("cantidad"));
                    dr.setDescuentoHistorico(rs.getDouble("descuento_historico"));
                    return dr;
                }
        );
        pedido.setDetallesRecetas(detRecetas);
    }

    private void saveDetails(Connection con, Pedido pedido) throws SQLException {
        ProductoSnapshotDAO productoSnapshotDAO = new ProductoSnapshotDAOImpl();
        RecetaSnapshotDAO recetaSnapshotDAO = new RecetaSnapshotDAOImpl();

        final String sqlProd = "INSERT INTO Pedido_Detalle_Producto (id_pedido, id_producto_snapshot, cantidad) VALUES (?, ?, ?)";
        if (pedido.getDetallesProductos() != null) {
            for (PedidoDetalleProducto dp : pedido.getDetallesProductos()) {
                if (dp.getProductoSnapshot() != null && (dp.getProductoSnapshot().getId() == null || dp.getProductoSnapshot().getId() == 0)) {
                    productoSnapshotDAO.save(con, dp.getProductoSnapshot());
                }
                DAOUtils.save(sqlProd, con, (ps) -> {
                    ps.setInt(1, pedido.getId());
                    ps.setInt(2, dp.getProductoSnapshot().getId());
                    ps.setInt(3, dp.getCantidad());
                }, (rs) -> dp.setId(rs.getInt(1)));
            }
        }

        final String sqlRec = "INSERT INTO Pedido_Detalle_Receta (id_pedido, id_receta_snapshot, cantidad, descuento_historico) VALUES (?, ?, ?, ?)";
        if (pedido.getDetallesRecetas() != null) {
            for (PedidoDetalleReceta dr : pedido.getDetallesRecetas()) {
                if (dr.getRecetaSnapshot() != null && (dr.getRecetaSnapshot().getId() == null || dr.getRecetaSnapshot().getId() == 0)) {
                    recetaSnapshotDAO.save(con, dr.getRecetaSnapshot());
                }
                DAOUtils.save(sqlRec, con, (ps) -> {
                    ps.setInt(1, pedido.getId());
                    ps.setInt(2, dr.getRecetaSnapshot().getId());
                    ps.setInt(3, dr.getCantidad());
                    ps.setDouble(4, dr.getDescuentoHistorico());
                }, (rs) -> dr.setId(rs.getInt(1)));
            }
        }
    }

    private void deleteDetails(Connection con, Integer idPedido) throws SQLException {
        final String sqlProd = "DELETE FROM Pedido_Detalle_Producto WHERE id_pedido = ?";
        DAOUtils.delete(sqlProd, con, (ps) -> ps.setInt(1, idPedido));

        final String sqlRec = "DELETE FROM Pedido_Detalle_Receta WHERE id_pedido = ?";
        DAOUtils.delete(sqlRec, con, (ps) -> ps.setInt(1, idPedido));
    }

    private Pedido mapPedido(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setId(rs.getInt("id_pedido"));

        Cliente cliente = new Cliente();
        cliente.setIdUsuario(rs.getInt("id_cliente"));
        pedido.setCliente(cliente);

        pedido.setFechaPedido(rs.getDate("fecha_pedido"));
        pedido.setHoraInicio(rs.getTime("hora_inicio"));
        pedido.setHoraFin(rs.getTime("hora_fin"));

        pedido.setPrecioTotal(rs.getDouble("precio_total"));
        pedido.setTotalImpuestos(rs.getDouble("total_impuestos"));
        pedido.setPrecioDelivery(rs.getDouble("precio_delivery"));

        pedido.setPrecioFinal(rs.getDouble("precio_final"));

        pedido.setEstado(EstadoPedido.valueOf(rs.getString("estado")));
        pedido.setDireccionDestino(rs.getString("direccion_destino"));

        return pedido;
    }

    private void preparerDeclaration(PreparedStatement ps, Pedido pedido) throws SQLException {
        ps.setInt(1, pedido.getCliente().getIdUsuario());
        ps.setDate(2, new java.sql.Date(pedido.getFechaPedido().getTime()));
        ps.setTime(3, new java.sql.Time(pedido.getHoraInicio().getTime()));
        ps.setTime(4, new java.sql.Time(pedido.getHoraFin().getTime()));
        ps.setDouble(5, pedido.getPrecioTotal());
        ps.setDouble(6, pedido.getTotalImpuestos());
        ps.setDouble(7, pedido.getPrecioDelivery());
        ps.setString(8, pedido.getEstado().name());
        ps.setString(9, pedido.getDireccionDestino());
    }
}