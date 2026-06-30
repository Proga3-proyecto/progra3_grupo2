package com.licoreria.app;

import ch.qos.logback.core.net.server.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.licoreria.BusinessLayer.carrito.PedidoBL;
import com.licoreria.BusinessLayer.carrito.PedidoBLImpl;
import com.licoreria.BusinessLayer.catalogo.ProductoBL;
import com.licoreria.BusinessLayer.catalogo.ProductoBLImpl;
import com.licoreria.dominio.carrito.Pedido;
import com.licoreria.dominio.catalogo.Producto;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        String apiKey = "sb_secret_6EJ5Hy9ueG3SdQAiDruj5g_91pSKMdh";

       HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .version(HttpClient.Version.HTTP_2)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://zrrvsajbasvpbtannzyc.supabase.co/storage/v1/object"))
                .header("apikey", apiKey)
                .header("Authorization", "Bearer " + apiKey)
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.statusCode());
        System.out.println(response.body());
    }
}