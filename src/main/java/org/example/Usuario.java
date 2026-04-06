package org.example;

import java.util.Date;

public abstract class Usuario {
    private int idUsuario;
    private int dni;
    private String nombre;
    private String apellidoCompleto;
    private Date fechaNacimiento;
    private Date fechaCreacionCuenta;
    private String correo;
    private String contraseñaHash;
    private int telefono;
    private EstadoCuenta estado;
}
