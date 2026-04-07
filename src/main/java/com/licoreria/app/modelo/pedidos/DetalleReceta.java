package com.licoreria.app.modelo.pedidos;

import com.licoreria.app.modelo.productos.Receta;

import java.util.List;

public class DetalleReceta extends DetallePedido {
    private Receta recetaBase;
    private List<DetalleElementoReceta> elementosDesglosados;
}
