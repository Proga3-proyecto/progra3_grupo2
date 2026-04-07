package com.licoreria.app.modelo.usuarios;

import com.licoreria.app.modelo.pedidos.Pedido;

import java.util.Date;
import java.util.List;

public abstract class Usuario {
    private List<Pedido> historialPedidos;
    private int idUsuario;
    private String dni;
    private String nombre;
    private String apellidoCompleto;
    private Date fechaNacimiento;
    private Date fechaCreacionCuenta;
    private String correo;
    private String contraseñaHash;
    private String telefono;
    private EstadoCuenta estado;
}
