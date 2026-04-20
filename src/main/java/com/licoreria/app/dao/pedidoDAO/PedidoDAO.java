package com.licoreria.app.dao.pedidoDAO;

import com.licoreria.app.modelo.pedidos.Pedido;
import java.util.List;

public interface PedidoDAO {
    Pedido get(long id);
    List<Pedido> getAll();

    long save(Pedido pedido);

    void update(Pedido pedido);
    void delete(long idPedido);
}