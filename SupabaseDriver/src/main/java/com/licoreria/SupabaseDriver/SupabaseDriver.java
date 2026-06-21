package com.licoreria.SupabaseDriver;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyStore;
import java.util.ResourceBundle;

public class SupabaseDriver {

    private final String supabaseUrl;
    private final String apiKey;
    private final String bucket;
    private final HttpClient client;

    public SupabaseDriver() {
        ResourceBundle rs = ResourceBundle.getBundle("supabase");
        supabaseUrl = rs.getString("supabase.url").replaceAll("/$", "");
        apiKey = rs.getString("supabase.key");
        bucket = rs.getString("supabase.bucket");

        // Inicializamos el cliente forzando la lectura de certificados de Windows
        client = buildWindowsSSLClient();
    }

    private HttpClient buildWindowsSSLClient() {
        try {
            // 1. Cargamos la bóveda de certificados nativa de Windows
            KeyStore trustStore = KeyStore.getInstance("Windows-ROOT");
            trustStore.load(null, null);

            // 2. Creamos un administrador de confianza basado en esa bóveda
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);

            // 3. Fabricamos un contexto SSL estricto y seguro
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            // 4. Inyectamos este contexto directamente en el cliente
            return HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .build();

        } catch (Exception e) {
            System.err.println("No se pudo cargar el KeyStore de Windows. Usando cliente por defecto.");
            return HttpClient.newHttpClient();
        }
    }

    public String upload(String fileName, InputStream fileStream) throws Exception {
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
        String path = bucket + "/" + encodedFileName;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(supabaseUrl + "/storage/v1/object/" + path))
                .header("Authorization", "Bearer " + apiKey) // Vital para evitar el error 401
                .header("apikey", apiKey)
                .header("Content-Type", "application/octet-stream")
                .header("x-upsert", "true")
                .PUT(HttpRequest.BodyPublishers.ofInputStream(() -> fileStream))
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new RuntimeException("Error Supabase HTTP " + response.statusCode() + ": " + response.body());
        }

        return getPublicUrl(encodedFileName);
    }

    public String getPublicUrl(String fileName) {
        return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + fileName;
    }
}