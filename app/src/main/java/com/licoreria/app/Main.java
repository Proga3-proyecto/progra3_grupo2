package com.licoreria.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.licoreria.BusinessLayer.carrito.PedidoBL;
import com.licoreria.BusinessLayer.carrito.PedidoBLImpl;
import com.licoreria.BusinessLayer.catalogo.ProductoBL;
import com.licoreria.BusinessLayer.catalogo.ProductoBLImpl;
import com.licoreria.dominio.carrito.Pedido;
import com.licoreria.dominio.catalogo.Producto;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        PedidoBL pedidoBL = new PedidoBLImpl();
        List<Pedido> pedidos = pedidoBL.getAll();
        ObjectMapper mapper = new ObjectMapper();

        try{
            String json = mapper.writeValueAsString(pedidos);

            System.out.println(json);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}