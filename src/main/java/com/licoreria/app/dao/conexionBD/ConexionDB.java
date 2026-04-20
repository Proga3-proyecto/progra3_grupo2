package com.licoreria.app.dao.conexionBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConexionDB {
    private static ConexionDB instance;
    private final Connection conexion;
    private final boolean conectado;
    private final String usuario;
    private final String password;

    private ConexionDB() throws SQLException {
        ResourceBundle db = ResourceBundle.getBundle("db");
        String dbURL = getDatabaseURL(db);
        this.usuario = db.getString("db.usuario");
        this.password = db.getString("db.password");
        this.conexion = DriverManager.getConnection(dbURL, usuario, password);
        this.conectado = conexion != null && !conexion.isClosed();
    }

    public static synchronized ConexionDB getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConexionDB();
        }
        return instance;
    }

    public Connection getConexion() {
        return conexion;
    }

    public boolean isConectado() {
        return conectado;
    }

    private String getDatabaseURL(ResourceBundle db) {
        String host = db.getString("db.host");
        int port = Integer.parseInt(db.getString("db.puerto"));
        String esquema = db.getString("db.esquema");
        return "jdbc:mysql://" + host + ":" + port + "/" + esquema;
    }
}
