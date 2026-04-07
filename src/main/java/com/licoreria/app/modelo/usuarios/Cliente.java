package com.licoreria.app.modelo.usuarios;

import com.licoreria.app.modelo.pedidos.DetalleProducto;
import com.licoreria.app.modelo.pedidos.DetalleReceta;
import com.licoreria.app.modelo.pedidos.Pedido;
import com.licoreria.app.modelo.productos.Producto;
import com.licoreria.app.modelo.productos.Receta;

import java.util.List;

public class Cliente extends Usuario {
    private List<Pedido> historialPedidos;

    private List<DetalleProducto> carritoProductos;
    private List<DetalleReceta> carritoRecetas;

    private Pedido pedigoActivo;
    private List<String> direcciones;

    private List<Producto> productosFavoritos;
    private List<Receta> recetasFavoritos;
}
