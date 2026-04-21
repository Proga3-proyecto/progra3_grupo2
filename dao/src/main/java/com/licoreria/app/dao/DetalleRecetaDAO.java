package com.licoreria.app.dao;

import com.licoreria.app.dominio.pedidos.DetalleReceta;

import java.util.List;

public interface DetalleRecetaDAO {
    List<DetalleReceta> getByCarrito(long idCliente);
    List<DetalleReceta> getByPedido(long idPedido);

    void save(DetalleReceta detalle, Long idPedido, Long idClienteCarrito);
    void update(DetalleReceta detalle, Long idPedido, Long idClienteCarrito);
    void delete(long idDetalleReceta);
}