package com.licoreria.app.modelo.usuarios;

import java.util.Date;

public class Admin extends Usuario {
    private Date fechaInicioAdmin;

    public Date getFechaInicioAdmin() {
        return fechaInicioAdmin;

    }

    public void setFechaInicioAdmin(Date fechaInicioAdmin) {
        this.fechaInicioAdmin = fechaInicioAdmin;
    }

    public Admin(){
        
    }

    public Admin(
            String dni,
            String nombre,
            String correo,
            String telefono,
            String apellidoCompleto,
            Date fechaNacimiento,
            String contraseniaHash) {

        super(dni, nombre, correo, telefono, apellidoCompleto, fechaNacimiento, contraseniaHash);
    }
}
