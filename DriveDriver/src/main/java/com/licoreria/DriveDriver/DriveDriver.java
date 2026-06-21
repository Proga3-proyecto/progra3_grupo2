package com.licoreria.DriveDriver;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.Collections;

public class DriveDriver {
    private static final String CREDENTIALS_FILE_PATH = "/drive-credentials.json";
    private static final String APPLICATION_NAME = "allasi_licores";

    // MODIFICADO: Ya no es final, se asignará dinámicamente
    private static String tokensDirectoryPath;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static HttpTransport HTTP_TRANSPORT;
    private static Drive instance;

    static {
        try {
            KeyStore windowsTrustStore = KeyStore.getInstance("Windows-ROOT");
            windowsTrustStore.load(null, null);

            HTTP_TRANSPORT = new NetHttpTransport.Builder()
                    .trustCertificates(windowsTrustStore)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Error inicializando HTTP_TRANSPORT con Windows-ROOT: " + e.getMessage(), e);
        }
    }

    // NUEVO: Método para que el entorno web configure la ruta exacta del proyecto
    public static void initTokensPath(String realPath) {
        tokensDirectoryPath = realPath;
    }

    public static synchronized Drive getInstance() {
        if (instance == null) {
            // Respaldo: Si no se ha inicializado desde la web (por ejemplo, al ejecutar el main),
            // usa la ruta local por defecto del proyecto.
            if (tokensDirectoryPath == null) {
                tokensDirectoryPath = System.getProperty("user.dir") + "/drive_tokens_allasi";
            }
            try {
                instance = buildDriveService();
            } catch (IOException e) {
                throw new RuntimeException("Error de Conexión: " + e.getMessage(), e);
            }
        }
        return instance;
    }

    private static Drive buildDriveService() throws IOException {
        InputStream in = DriveDriver.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("No se encontró el archivo de credenciales en: " + CREDENTIALS_FILE_PATH);
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, Collections.singletonList(DriveScopes.DRIVE))
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(tokensDirectoryPath)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static String uploadInputStream(InputStream inputStream, String fileName, String mimeType, String parentFolderId) throws IOException {
        Drive service = getInstance();
        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        fileMetadata.setParents(Collections.singletonList(parentFolderId));
        InputStreamContent mediaContent = new InputStreamContent(mimeType, inputStream);
        File uploadedFile = service.files().create(fileMetadata, mediaContent)
                .setFields("id, name")
                .execute();
        String fileId = uploadedFile.getId();
        System.out.println("Archivo subido exitosamente. ID: " + fileId);
        String directImageUrl = "https://drive.google.com/uc?export=view&id=" + fileId;
        System.out.println("URL directa para imagen: " + directImageUrl);
        return directImageUrl;
    }

    public static void deleteFileByUrl(String fileUrl) throws IOException {
        if (fileUrl == null || !fileUrl.contains("id=")) {
            throw new IllegalArgumentException("La URL proporcionada no es válida o no contiene un ID.");
        }
        String fileId = fileUrl.substring(fileUrl.indexOf("id=") + 3);
        if (fileId.contains("&")) {
            fileId = fileId.substring(0, fileId.indexOf("&"));
        }
        Drive service = getInstance();
        try {
            service.files().delete(fileId).execute();
            System.out.println("Archivo eliminado exitosamente de Drive. ID: " + fileId);
        } catch (IOException e) {
            System.err.println("Error al intentar eliminar el archivo con ID " + fileId);
            throw e;
        }
    }
//    public static void main(String[] args) {
//        try {
//            System.out.println("Iniciando autorización manual...");
//            Drive service = getInstance();
//            System.out.println("¡Autorización exitosa! El token permanente se ha guardado en: " + tokensDirectoryPath);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}