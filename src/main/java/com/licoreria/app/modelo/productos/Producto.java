package com.licoreria.app.modelo.productos;

import com.licoreria.app.modelo.pedidos.Pedido;

import java.util.List;

public class Producto {
    private int idProducto;
    private String  nombre;
    private double precio;
    private int stock;
    private double descuento;
    private String imagenURL;
    private boolean paraCombos;

    public static class SistemaPedidos {
        private List<Pedido> pedidos;

    }
}
