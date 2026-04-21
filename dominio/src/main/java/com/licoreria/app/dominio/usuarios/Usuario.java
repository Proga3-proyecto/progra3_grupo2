package com.licoreria.app.dominio.usuarios;


import com.licoreria.app.dominio.pedidos.Pedido;

import java.util.Date;
import java.util.List;

public abstract class Usuario {
    private long id;
    private List<Pedido> historialPedidos;
    private String dni;
    private String nombre;
    private String apellidoCompleto;
    private Date fechaNacimiento;
    private Date fechaCreacionCuenta;
    private String correo;
    private String contraseniaHash;
    private String telefono;
    private EstadoCuenta estado;

    public Usuario() {
    }


    public Usuario(
            String dni,
            String nombre,
            String correo,
            String telefono,
            String apellidoCompleto,
            Date fechaNacimiento,
            String contraseniaHash) {

        this.dni = dni;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.apellidoCompleto = apellidoCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.contraseniaHash = contraseniaHash;
        this.estado = EstadoCuenta.ACTIVA;
    }

    public List<Pedido> getHistorialPedidos() {
        return historialPedidos;
    }

    public void setHistorialPedidos(List<Pedido> historialPedidos) {
        this.historialPedidos = historialPedidos;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoCompleto() {
        return apellidoCompleto;
    }

    public void setApellidoCompleto(String apellidoCompleto) {
        this.apellidoCompleto = apellidoCompleto;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Date getFechaCreacionCuenta() {
        return fechaCreacionCuenta;
    }

    public void setFechaCreacionCuenta(Date fechaCreacionCuenta) {
        this.fechaCreacionCuenta = fechaCreacionCuenta;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseniaHash() {
        return contraseniaHash;
    }

    public void setContraseniaHash(String contraseniaHash) {
        this.contraseniaHash = contraseniaHash;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public EstadoCuenta getEstado() {
        return estado;
    }

    public void setEstado(EstadoCuenta estado) {
        this.estado = estado;
    }
}
