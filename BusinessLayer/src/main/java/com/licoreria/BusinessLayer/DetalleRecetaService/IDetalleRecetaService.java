package com.licoreria.BusinessLayer.DetalleRecetaService;

import com.licoreria.dominio.pedidos.DetalleReceta;
import com.licoreria.dominio.productos.Receta;

import java.util.List;

public interface IDetalleRecetaService  {
    DetalleReceta obtenerPorId(Long id);
    List<DetalleReceta> listarTodos();
    DetalleReceta crearDesdeReceta(Receta receta, Long idPedido, Long idClienteCarrito);
    DetalleReceta actualizar(DetalleReceta detalle);
    void eliminar(DetalleReceta detalle);
    List<DetalleReceta> obtenerPorPedido(Long idPedido);
    List<DetalleReceta> obtenerPorCarrito(Long idCliente);
    void recalcularMonto(DetalleReceta detalle);
}