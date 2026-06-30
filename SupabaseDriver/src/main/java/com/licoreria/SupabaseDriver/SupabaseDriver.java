package com.licoreria.SupabaseDriver;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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


        client = buildInsecureSSLClient();
    }

    private HttpClient buildInsecureSSLClient() {
        try {

            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {

                        }
                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {

                        }
                    }
            };


            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());


            return HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .version(HttpClient.Version.HTTP_2)
                    .sslContext(sslContext)
                    .build();

        } catch (Exception e) {
            System.err.println("Error configurando SSL bypass. Usando cliente por defecto.");
            return HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .version(HttpClient.Version.HTTP_2)
                    .build();
        }
    }

    public String upload(String fileName, InputStream fileStream) throws Exception {
        String extension = "";
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = fileName.substring(dotIndex);
        }

        String randomFileName = java.util.UUID.randomUUID().toString() + extension;
        String encodedFileName = URLEncoder.encode(randomFileName, StandardCharsets.UTF_8).replace("+", "%20");
        String path = bucket + "/" + encodedFileName;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(supabaseUrl + "/storage/v1/object/" + path))
                .header("Authorization", "Bearer " + apiKey)
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

    public void delete(String fileName) throws Exception {
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
        String path = bucket + "/" + encodedFileName;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(supabaseUrl + "/storage/v1/object/" + path))
                .header("Authorization", "Bearer " + apiKey)
                .header("apikey", apiKey)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200 && response.statusCode() != 204) {
            throw new RuntimeException("Error eliminando archivo HTTP " + response.statusCode() + ": " + response.body());
        }
    }

    public String getPublicUrl(String fileName) {
        return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + fileName;
    }
}