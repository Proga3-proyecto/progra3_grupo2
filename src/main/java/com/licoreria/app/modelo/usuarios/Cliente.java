package com.licoreria.app.modelo.usuarios;

import com.licoreria.app.modelo.pedidos.Pedido;

import java.util.List;

public class Cliente extends Usuario {
    private List<Pedido> historialPedidos;
    private String direccion;
}
