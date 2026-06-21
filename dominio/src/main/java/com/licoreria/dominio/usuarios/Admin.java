package com.licoreria.dominio.usuarios;

import java.util.Date;

public class Admin extends Usuario {

    private Date fechaInicioAdmin;

    public Admin() {
    }

    public Date getFechaInicioAdmin() {
        return fechaInicioAdmin;
    }

    public void setFechaInicioAdmin(Date fechaInicioAdmin) {
        this.fechaInicioAdmin = fechaInicioAdmin;
    }

}