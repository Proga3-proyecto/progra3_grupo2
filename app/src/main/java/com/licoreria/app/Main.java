package com.licoreria.app;

import com.licoreria.BusinessLayer.catalogo.ProductoBL;
import com.licoreria.BusinessLayer.catalogo.ProductoBLImpl;
import com.licoreria.dominio.catalogo.Producto;
import com.licoreria.dto.ProductoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.blackbird.BlackbirdModule;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        ProductoBL productoBL = new ProductoBLImpl();
        productoBL.getAll(); // calentar conexiones

        // 1. Obtener todos los productos (simula tu endpoint GET /productos)
        long t0 = System.currentTimeMillis();
        List<Producto> productos = productoBL.getAll();
        long t1 = System.currentTimeMillis();
        System.out.println("1. BO (obtener todos): " + (t1 - t0) + " ms");

        // 2. Mapeo a ProductoDTO
        long t2 = System.currentTimeMillis();
        List<ProductoDTO> dtos = productos.stream()
                .map(ProductoDTO::new)   // usa el constructor ProductoDTO(Producto p)
                .toList();
        long t3 = System.currentTimeMillis();
        System.out.println("2. Mapeo a DTO: " + (t3 - t2) + " ms");

        // 3. Serialización con Blackbird
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new BlackbirdModule());

        // ***** CALENTAMIENTO (no se mide) *****
        mapper.writeValueAsString(dtos);  // genera bytecode optimizado

        // ***** MEDICIÓN REAL (2 ejecuciones) *****
        for (int i = 1; i <= 2; i++) {
            long t4 = System.nanoTime();
            String json = mapper.writeValueAsString(dtos);
            long t5 = System.nanoTime();
            double ms = (t5 - t4) / 1_000_000.0;
            System.out.printf("3.%d Serialización JSON: %.2f ms (tamaño: %d caracteres)%n",
                    i, ms, json.length());
        }
    }
}