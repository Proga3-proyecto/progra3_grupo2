package com.licoreria.dto;

public class LoginResponse {
    private int idUsuario;
    private String nombre;
    private String rol;
    private boolean exito;
    private String mensaje;

    // Constructor para login exitoso
    public LoginResponse(int idUsuario, String nombre, String rol, boolean exito, String mensaje) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.rol = rol;
        this.exito = exito;
        this.mensaje = mensaje;
    }

    // Constructor para login fallido
    public LoginResponse(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }

    // Getters y Setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public boolean isExito() { return exito; }
    public void setExito(boolean exito) { this.exito = exito; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
