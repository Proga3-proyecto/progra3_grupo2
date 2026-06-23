package com.licoreria.dao.usuarios;

import com.licoreria.dao.DAOUtils;
import com.licoreria.dominio.carrito.Pedido;
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
                "c.telefono, c.fecha_nacimiento, c.id_pedido_activo " +
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
                "c.telefono, c.fecha_nacimiento, c.id_pedido_activo " +
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
        }, (rs) -> {});

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
                    dir.setCliente(cliente);
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
                "c.telefono, c.fecha_nacimiento, c.id_pedido_activo " +
                "FROM Cliente c " +
                "INNER JOIN Usuario u ON c.id_usuario = u.id_usuario " +
                "WHERE u.correo = ? AND u.contrasena_hash = ?";

        Cliente cliente = DAOUtils.get(sql, con, (ps) -> {ps.setString(1, correo); ps.setString(2, contrasena);}, (rs) -> mapearCliente(rs));

        if (cliente != null) {
            cargarDirecciones(con, cliente);
        }

        return cliente;
    }

}