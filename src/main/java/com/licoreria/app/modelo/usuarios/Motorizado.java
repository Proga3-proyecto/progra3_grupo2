package com.licoreria.app.modelo.usuarios;

import com.licoreria.app.modelo.pedidos.Pedido;

import java.util.Date;
import java.util.List;

public class Motorizado extends Usuario {
    private String placa;
    private double horasTrabajo;
    private List<Pedido> pedidosAsignados;
    private double pagoMensual;

    public Motorizado(
            String dni,
            String nombre,
            String correo,
            String telefono,
            String apellidoCompleto,
            Date fechaNacimiento,
            String contraseniaHash,
            String placa,
            double horasTrabajo,
            double pagoMensual
            ) {
        super(dni, nombre, correo, telefono, apellidoCompleto, fechaNacimiento, contraseniaHash);
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public double getHorasTrabajo() {
        return horasTrabajo;
    }

    public void setHorasTrabajo(double horasTrabajo) {
        this.horasTrabajo = horasTrabajo;
    }

    public List<Pedido> getPedidosAsignados() {
        return pedidosAsignados;
    }

    public void setPedidosAsignados(List<Pedido> pedidosAsignados) {
        this.pedidosAsignados = pedidosAsignados;
    }

    public double getPagoMensual() {
        return pagoMensual;
    }

    public void setPagoMensual(double pagoMensual) {
        this.pagoMensual = pagoMensual;
    }
}
