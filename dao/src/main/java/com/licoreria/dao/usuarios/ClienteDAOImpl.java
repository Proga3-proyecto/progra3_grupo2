package com.licoreria.dao.usuarios;

import com.licoreria.dao.DAOUtils;
import com.licoreria.dominio.carrito.DetalleProducto;
import com.licoreria.dominio.carrito.DetalleReceta;
import com.licoreria.dominio.carrito.Pedido;
import com.licoreria.dominio.catalogo.Producto;
import com.licoreria.dominio.catalogo.Receta;
import com.licoreria.dominio.usuarios.Cliente;
import com.licoreria.dominio.usuarios.ClienteDireccion;
import com.licoreria.dominio.usuarios.EstadoUsuario;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public Cliente get(Connection con, Integer id) throws SQLException {
        final String sql = "SELECT u.id_usuario, u.dni, u.nombre, u.apellido_completo, u.correo, u.contrasena_hash, u.estado, " +
                "c.telefono, c.fecha_nacimiento, c.id_pedido_activo, u.created_at " +
                "FROM Cliente c " +
                "INNER JOIN Usuario u ON c.id_usuario = u.id_usuario " +
                "WHERE c.id_usuario = ?";

        Cliente cliente = DAOUtils.get(sql, con, (ps) -> ps.setInt(1, id), (rs) -> mapearCliente(rs));

        if (cliente != null) {
            cargarDirecciones(con, cliente);
        }

        return cliente;
    }

    @Override
    public List<Cliente> getAll(Connection con) throws SQLException {
        final String sql = "SELECT u.id_usuario, u.dni, u.nombre, u.apellido_completo, u.correo, u.contrasena_hash, u.estado, " +
                "c.telefono, c.fecha_nacimiento, c.id_pedido_activo, u.created_at " +
                "FROM Cliente c " +
                "INNER JOIN Usuario u ON c.id_usuario = u.id_usuario";

        List<Cliente> clientes = DAOUtils.getAll(sql, con, (rs) -> mapearCliente(rs));

        for (Cliente cliente : clientes) {
            cargarDirecciones(con, cliente);
        }

        return clientes;
    }

    @Override
    public Cliente save(Connection con, Cliente cliente) throws SQLException {
        final String sqlUsuario = "INSERT INTO Usuario (dni, nombre, apellido_completo, correo, contrasena_hash, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        DAOUtils.save(sqlUsuario, con, (ps) -> {
            ps.setString(1, cliente.getDni());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getApellidoCompleto());
            ps.setString(4, cliente.getCorreo());
            ps.setString(5, cliente.getContrasenaHash());
            ps.setString(6, cliente.getEstado().name());
        }, (rs) -> {
            cliente.setIdUsuario(rs.getInt(1));
        });

        final String sqlCliente = "INSERT INTO Cliente (id_usuario, telefono, fecha_nacimiento, id_pedido_activo) " +
                "VALUES (?, ?, ?, ?)";

        DAOUtils.save(sqlCliente, con, (ps) -> {
            ps.setInt(1, cliente.getIdUsuario());
            ps.setString(2, cliente.getTelefono());
            ps.setDate(3, new java.sql.Date(cliente.getFechaNacimiento().getTime()));

            if (cliente.getPedidoActivo() != null && cliente.getPedidoActivo().getId() != null) {
                ps.setInt(4, cliente.getPedidoActivo().getId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
        }, (rs) -> {
        });

        if (cliente.getIdUsuario() != null && cliente.getDirecciones() != null && !cliente.getDirecciones().isEmpty()) {
            guardarDirecciones(con, cliente);
        }

        return cliente;
    }

    @Override
    public Cliente update(Connection con, Cliente cliente) throws SQLException {
        final String sqlUsuario = "UPDATE Usuario SET dni = ?, nombre = ?, apellido_completo = ?, correo = ?, " +
                "contrasena_hash = ?, estado = ? WHERE id_usuario = ?";
        DAOUtils.update(sqlUsuario, con, (ps) -> {
            ps.setString(1, cliente.getDni());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getApellidoCompleto());
            ps.setString(4, cliente.getCorreo());
            ps.setString(5, cliente.getContrasenaHash());
            ps.setString(6, cliente.getEstado().name());
            ps.setInt(7, cliente.getIdUsuario());
        });

        final String sqlCliente = "UPDATE Cliente SET telefono = ?, fecha_nacimiento = ?, id_pedido_activo = ? " +
                "WHERE id_usuario = ?";
        DAOUtils.update(sqlCliente, con, (ps) -> {
            ps.setString(1, cliente.getTelefono());
            ps.setDate(2, new java.sql.Date(cliente.getFechaNacimiento().getTime()));

            if (cliente.getPedidoActivo() != null && cliente.getPedidoActivo().getId() != null) {
                ps.setInt(3, cliente.getPedidoActivo().getId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setInt(4, cliente.getIdUsuario());
        });

        eliminarDirecciones(con, cliente.getIdUsuario());
        if (cliente.getDirecciones() != null && !cliente.getDirecciones().isEmpty()) {
            guardarDirecciones(con, cliente);
        }

        return cliente;
    }

    @Override
    public void remove(Connection con, Cliente cliente) throws SQLException {
        eliminarDirecciones(con, cliente.getIdUsuario());
        final String sql = "DELETE FROM Usuario WHERE id_usuario = ?";
        DAOUtils.delete(sql, con, (ps) -> ps.setInt(1, cliente.getIdUsuario()));
    }


    private void cargarDirecciones(Connection con, Cliente cliente) throws SQLException {
        final String sql = "SELECT id_direccion, direccion FROM Cliente_Direccion WHERE id_cliente = ?";

        List<ClienteDireccion> direcciones = DAOUtils.getAll(sql, con,
                (ps) -> ps.setInt(1, cliente.getIdUsuario()),
                (rs) -> {
                    ClienteDireccion dir = new ClienteDireccion();
                    dir.setId(rs.getInt("id_direccion"));
                    // dir.setCliente(cliente);
                    dir.setDireccion(rs.getString("direccion"));
                    return dir;
                }
        );

        cliente.setDirecciones(direcciones);
    }

    private void guardarDirecciones(Connection con, Cliente cliente) throws SQLException {
        final String sql = "INSERT INTO Cliente_Direccion (id_cliente, direccion) VALUES (?, ?)";

        for (ClienteDireccion dir : cliente.getDirecciones()) {
            DAOUtils.save(sql, con, (ps) -> {
                ps.setInt(1, cliente.getIdUsuario());
                ps.setString(2, dir.getDireccion());
            }, (rs) -> {
                dir.setId(rs.getInt(1));
            });
        }
    }

    private void eliminarDirecciones(Connection con, Integer idCliente) throws SQLException {
        final String sql = "DELETE FROM Cliente_Direccion WHERE id_cliente = ?";
        DAOUtils.delete(sql, con, (ps) -> ps.setInt(1, idCliente));
    }

    private Cliente mapearCliente(java.sql.ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setIdUsuario(rs.getInt("id_usuario"));
        cliente.setDni(rs.getString("dni"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setApellidoCompleto(rs.getString("apellido_completo"));
        cliente.setCorreo(rs.getString("correo"));
        cliente.setContrasenaHash(rs.getString("contrasena_hash"));
        cliente.setEstado(EstadoUsuario.valueOf(rs.getString("estado")));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
        cliente.setCreatedAt(rs.getDate("created_at"));
        int idPedidoActivo = rs.getInt("id_pedido_activo");
        if (!rs.wasNull()) {
            Pedido pedido = new Pedido();
            pedido.setId(idPedidoActivo);
            cliente.setPedidoActivo(pedido);
        }

        return cliente;
    }

    @Override
    public Cliente getPorCorreo(Connection con, String correo, String contrasena) throws SQLException {
        final String sql = "SELECT u.id_usuario, u.dni, u.nombre, u.apellido_completo, u.correo, u.contrasena_hash, u.estado, " +
                "c.telefono, c.fecha_nacimiento, c.id_pedido_activo, u.created_at " +
                "FROM Cliente c " +
                "INNER JOIN Usuario u ON c.id_usuario = u.id_usuario " +
                "WHERE u.correo = ? AND u.contrasena_hash = ?";

        Cliente cliente = DAOUtils.get(sql, con, (ps) -> {
            ps.setString(1, correo);
            ps.setString(2, contrasena);
        }, (rs) -> mapearCliente(rs));

        if (cliente != null) {
            cargarDirecciones(con, cliente);
        }

        return cliente;
    }

    @Override
    public void agregarProductoAlCarrito(Connection con, int idCliente, int idProducto, int cantidad, double descuentoTotal, double montoTotal) throws SQLException {
        // Verificar si el producto ya está en el carrito
        final String sqlCheck = "SELECT cantidad, descuento_total, monto_total FROM Detalle_Producto WHERE id_cliente_carrito = ? AND id_producto = ?";
        boolean exists = false;
        int currentCantidad = 0;
        double currentDescuento = 0;
        double currentMonto = 0;

        try (java.sql.PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {
            psCheck.setInt(1, idCliente);
            psCheck.setInt(2, idProducto);
            try (java.sql.ResultSet rs = psCheck.executeQuery()) {
                if (rs.next()) {
                    exists = true;
                    currentCantidad = rs.getInt("cantidad");
                    currentDescuento = rs.getDouble("descuento_total");
                    currentMonto = rs.getDouble("monto_total");
                }
            }
        }

        if (exists) {
            final int finalCantidad = currentCantidad;
            final double finalDescuento = currentDescuento;
            final double finalMonto = currentMonto;
            final String sqlUpdate = "UPDATE Detalle_Producto SET cantidad = ?, descuento_total = ?, monto_total = ? WHERE id_cliente_carrito = ? AND id_producto = ?";
            int finalCurrentCantidad = currentCantidad;
            double finalCurrentDescuento = currentDescuento;
            double finalCurrentMonto = currentMonto;
            DAOUtils.update(sqlUpdate, con, (ps) -> {
                ps.setInt(1, finalCurrentCantidad + cantidad);
                ps.setDouble(2, finalCurrentDescuento + descuentoTotal);
                ps.setDouble(3, finalCurrentMonto + montoTotal);
                ps.setInt(4, idCliente);
                ps.setInt(5, idProducto);
            });
        } else {
            final String sqlInsert = "INSERT INTO Detalle_Producto (id_cliente_carrito, id_producto, cantidad, descuento_total, monto_total) VALUES (?, ?, ?, ?, ?)";
            DAOUtils.update(sqlInsert, con, (ps) -> {
                ps.setInt(1, idCliente);
                ps.setInt(2, idProducto);
                ps.setInt(3, cantidad);
                ps.setDouble(4, descuentoTotal);
                ps.setDouble(5, montoTotal);
            });
        }
    }

    @Override
    public List<DetalleProducto> getDetalleProductos(Connection con, int idCliente) throws SQLException {
        final String sql = "SELECT cantidad, descuento_total, monto_total, id_producto FROM Detalle_Producto WHERE id_cliente_carrito = ?";

        return DAOUtils.getAll(sql, con, (ps) -> {
            ps.setInt(1, idCliente);
        }, (rs) -> {
            DetalleProducto detalle = new DetalleProducto();
            Producto producto = new Producto();
            producto.setId(rs.getInt("id_producto"));
            detalle.setProducto(producto);
            detalle.setCantidad(rs.getInt("cantidad"));
            detalle.setMontoTotal(rs.getInt("monto_total"));
            detalle.setDescuentoTotal(rs.getDouble("descuento_total"));
            return detalle;
        });
    }

    @Override
    public List<DetalleReceta> getDetalleReceta(Connection con, int idCliente) throws SQLException {
        final String sql = "SELECT cantidad, descuento_total, monto_total, id_receta FROM Detalle_Receta WHERE id_cliente_carrito = ?";

        return DAOUtils.getAll(sql, con, (ps) -> {
            ps.setInt(1, idCliente);
        }, (rs) -> {
            DetalleReceta detalle = new DetalleReceta();
            Receta receta = new Receta();
            receta.setId(rs.getInt("id_receta"));
            detalle.setReceta(receta);
            detalle.setCantidad(rs.getInt("cantidad"));
            detalle.setMontoTotal(rs.getInt("monto_total"));
            detalle.setDescuentoTotal(rs.getDouble("descuento_total"));
            return detalle;
        });
    }

    @Override
    public void agregarRecetaAlCarrito(Connection con, int idCliente, int idReceta, int cantidad, double descuentoTotal, double montoTotal) throws SQLException {
        final String sqlCheck = "SELECT cantidad, descuento_total, monto_total FROM Detalle_Receta WHERE id_cliente_carrito = ? AND id_receta = ?";
        boolean exists = false;
        int currentCantidad = 0;
        double currentDescuento = 0;
        double currentMonto = 0;

        try (java.sql.PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {
            psCheck.setInt(1, idCliente);
            psCheck.setInt(2, idReceta);
            try (java.sql.ResultSet rs = psCheck.executeQuery()) {
                if (rs.next()) {
                    exists = true;
                    currentCantidad = rs.getInt("cantidad");
                    currentDescuento = rs.getDouble("descuento_total");
                    currentMonto = rs.getDouble("monto_total");
                }
            }
        }

        if (exists) {
            final int finalCantidad = currentCantidad;
            final double finalDescuento = currentDescuento;
            final double finalMonto = currentMonto;
            final String sqlUpdate = "UPDATE Detalle_Receta SET cantidad = ?, descuento_total = ?, monto_total = ? WHERE id_cliente_carrito = ? AND id_receta = ?";
            int finalCurrentCantidad = currentCantidad;
            double finalCurrentDescuento = currentDescuento;
            double finalCurrentMonto = currentMonto;
            DAOUtils.update(sqlUpdate, con, (ps) -> {
<<<<<<< HEAD
                ps.setInt(1, finalCurrentCantidad + cantidad);
                ps.setDouble(2, finalCurrentDescuento + descuentoTotal);
                ps.setDouble(3, finalCurrentMonto + montoTotal);
=======
                ps.setInt(1, finalCantidad + cantidad);
                ps.setDouble(2, finalDescuento + descuentoTotal);
                ps.setDouble(3, finalMonto + montoTotal);
>>>>>>> ed6ff7c7dbd0ee4df0a0ae00392542ff694c4f3d
                ps.setInt(4, idCliente);
                ps.setInt(5, idReceta);
            });
        } else {
            final String sqlInsert = "INSERT INTO Detalle_Receta (id_cliente_carrito, id_receta, cantidad, descuento_total, monto_total) VALUES (?, ?, ?, ?, ?)";
            DAOUtils.update(sqlInsert, con, (ps) -> {
                ps.setInt(1, idCliente);
                ps.setInt(2, idReceta);
                ps.setInt(3, cantidad);
                ps.setDouble(4, descuentoTotal);
                ps.setDouble(5, montoTotal);
            });
        }
    }

    @Override
    public void eliminarProductoDelCarrito(Connection con, int idCliente, int idProducto) throws SQLException {
        final String sql = "DELETE FROM Detalle_Producto WHERE id_cliente_carrito = ? AND id_producto = ?";
        DAOUtils.delete(sql, con, (ps) -> {
            ps.setInt(1, idCliente);
            ps.setInt(2, idProducto);
        });
    }

    @Override
    public void eliminarRecetaDelCarrito(Connection con, int idCliente, int idReceta) throws SQLException {
        final String sql = "DELETE FROM Detalle_Receta WHERE id_cliente_carrito = ? AND id_receta = ?";
        DAOUtils.delete(sql, con, (ps) -> {
            ps.setInt(1, idCliente);
            ps.setInt(2, idReceta);
        });
    }

    @Override
    public void actualizarCantidadProductoEnCarrito(Connection con, int idCliente, int idProducto, int cantidad, double descuentoTotal, double montoTotal) throws SQLException {
        final String sql = "UPDATE Detalle_Producto SET cantidad = ?, descuento_total = ?, monto_total = ? WHERE id_cliente_carrito = ? AND id_producto = ?";
        DAOUtils.update(sql, con, (ps) -> {
            ps.setInt(1, cantidad);
            ps.setDouble(2, descuentoTotal);
            ps.setDouble(3, montoTotal);
            ps.setInt(4, idCliente);
            ps.setInt(5, idProducto);
        });
    }

    @Override
    public void actualizarCantidadRecetaEnCarrito(Connection con, int idCliente, int idReceta, int cantidad, double descuentoTotal, double montoTotal) throws SQLException {
        final String sql = "UPDATE Detalle_Receta SET cantidad = ?, descuento_total = ?, monto_total = ? WHERE id_cliente_carrito = ? AND id_receta = ?";
        DAOUtils.update(sql, con, (ps) -> {
            ps.setInt(1, cantidad);
            ps.setDouble(2, descuentoTotal);
            ps.setDouble(3, montoTotal);
            ps.setInt(4, idCliente);
            ps.setInt(5, idReceta);
        });
    }

}