package com.licoreria.dominio.usuarios;

import java.util.Date;

public class Admin extends Usuario {

    private Date fechaInicioAdmin;
    private boolean isMaster;

    public Admin() {
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }


    public Date getFechaInicioAdmin() {
        return fechaInicioAdmin;
    }

    public void setFechaInicioAdmin(Date fechaInicioAdmin) {
        this.fechaInicioAdmin = fechaInicioAdmin;
    }

}