package org.example;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class Pedido {
    private int idPedido;
    private Cliente cliente;
    private Motorizado motorizado;
    private String  direccion;
    private Date fechaPedido;
    private List<DetalleProducto> productos;
    private List<DetalleCombo> combos;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private double precioTotal;
    private double precioDelivery;
    private EstadoPedido estado;
    private String direccionDestino;
}
