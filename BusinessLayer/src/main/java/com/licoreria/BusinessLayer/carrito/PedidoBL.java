package com.licoreria.BusinessLayer.carrito;

import com.licoreria.dominio.carrito.EstadoPedido;
import com.licoreria.dominio.carrito.Pedido;

import java.util.List;

public interface PedidoBL {
    List<Pedido> getAll();
    Pedido get(int id);
    Pedido save(Pedido pedido);
    Pedido update(Pedido pedido);
    void delete(int id);

    List<Pedido> getPedidosPorCliente(int idCliente);
    List<Pedido> getPedidosPorEstado(EstadoPedido estado);
    void actualizarEstadoPedido(int idPedido, EstadoPedido nuevoEstado);
}