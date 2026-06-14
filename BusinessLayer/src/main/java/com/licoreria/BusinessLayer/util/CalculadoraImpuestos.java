package com.licoreria.BusinessLayer.util;

import com.licoreria.dominio.productos.Impuesto;
import com.licoreria.dominio.productos.Producto;
import com.licoreria.dominio.productos.TipoImpuesto;

public class CalculadoraImpuestos {
    public static double calcularTotalImpuestos(Producto producto, int cantidad, double precioBase) {
        double totalImpuestos = 0.0;
        if (producto.getImpuestos() == null) return totalImpuestos;
        for (Impuesto imp : producto.getImpuestos()) {
            if (!imp.getActivo()) continue;
            double valor = imp.getValor();
            if (imp.getTipo() == TipoImpuesto.PORCENTAJE) {
                totalImpuestos += precioBase * cantidad * (valor / 100.0);
            } else {
                totalImpuestos += valor * cantidad;
            }
        }
        return totalImpuestos;
    }
}
