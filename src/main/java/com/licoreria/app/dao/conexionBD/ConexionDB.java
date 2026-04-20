package com.licoreria.app.dao.conexionBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConexionDB {
    private static ConexionDB instance;
    private static final ResourceBundle db = ResourceBundle.getBundle("db");

    public static Connection getConexion() throws SQLException {
        String url = getDatabaseURL();

        return DriverManager.getConnection(
                url,
                db.getString("db.usuario"),
                db.getString("db.password")
        );
    }

    private static  String getDatabaseURL() {
        String host = db.getString("db.host");
        int port = Integer.parseInt(db.getString("db.puerto"));
        String esquema = db.getString("db.esquema");
        return "jdbc:mysql://" + host + ":" + port + "/" + esquema;
    }
}
