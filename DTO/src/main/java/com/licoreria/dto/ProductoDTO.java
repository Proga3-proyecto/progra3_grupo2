package com.licoreria.dto;

import com.licoreria.dominio.catalogo.Producto;
import java.util.List;

public record ProductoDTO(
        Integer id,
        String nombre,
        double precio,
        double precioFinal,
        Integer stock,
        double descuento,
        double volumenLitros,
        double porcentajeAlcohol,
        String descripcion,
        String marca,
        List<String> categorias,
        String imagenPrincipal
) {
    public ProductoDTO(Producto p) {
        this(
                p.getId(),
                p.getNombre(),
                p.getPrecio(),
                p.getPrecioFinal(),
                p.getStock(),
                p.getDescuento(),
                p.getVolumenLitros(),
                p.getPorcentajeAlcohol(),
                p.getDescripcion(),
                p.getMarca() != null ? p.getMarca().getNombre() : null,
                p.getCategorias() != null ? p.getCategorias().stream().map(c -> c.getNombre()).toList() : List.of(),
                p.getImagenes() != null ? p.getImagenes().stream()
                        .filter(img -> img.isPrincipal())
                        .map(img -> img.getUrl())
                        .findFirst()
                        .orElse(null) : null
        );
    }
}