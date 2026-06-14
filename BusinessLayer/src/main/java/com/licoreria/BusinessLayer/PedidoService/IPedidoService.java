package com.licoreria.BusinessLayer.PedidoService;

import com.licoreria.dominio.pedidos.Pedido;
import com.licoreria.dominio.usuarios.Cliente;

import java.util.List;

public interface IPedidoService  {
    Pedido obtenerPorId(Long id);
    List<Pedido> listarTodos();
    Pedido crearPedidoDesdeCarrito(Cliente cliente, String direccionDestino);
    Pedido actualizar(Pedido pedido);
    void cancelarPedido(Pedido pedido);
    void cambiarEstado(Pedido pedido, String nuevoEstado);
    List<Pedido> obtenerHistorialCliente(Cliente cliente);
}