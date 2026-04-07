package com.licoreria.app.modelo.usuarios;

import com.licoreria.app.modelo.pedidos.Pedido;

import java.util.List;

public class Motorizado extends Usuario {
    private String placa;
    private int horasTrabajo;
    private List<Pedido> pedidosAsignados;
    private double pagoMensual;
}
