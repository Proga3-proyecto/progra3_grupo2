package com.licoreria.app;

//import com.licoreria.BusinessLayer.AdminService.AdminServiceImpl;
//import com.licoreria.BusinessLayer.AdminService.IAdminService;
//import com.licoreria.dominio.usuarios.Admin;
//import com.licoreria.dominio.usuarios.EstadoCuenta;

import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws Exception {
        URL url = new URL("https://oauth2.googleapis.com/token");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

        System.out.println("Response code: " + con.getResponseCode());
    }
}