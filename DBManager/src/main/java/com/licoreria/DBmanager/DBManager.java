package com.licoreria.DBmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DBManager {
    private static final ResourceBundle db = ResourceBundle.getBundle("db");
    private static DBManager instance;
    private final String url;
    private final String user;
    private final String password;

    private DBManager() {
        this.url = getDatabaseURL();
        this.user = db.getString("db.usuario");
        this.password = db.getString("db.password");

    }

    private String getDatabaseURL() {
        String host = db.getString("db.host");
        int port = Integer.parseInt(db.getString("db.puerto"));
        String esquema = db.getString("db.esquema");
        return "jdbc:mysql://" + host + ":" + port + "/" + esquema;
    }

    public static DBManager getInstance(){
        if(instance == null)
            instance = new DBManager();
        return instance;
    }

    public Connection getConnection(){
        try{
            return DriverManager.getConnection(url, user, password);
        }catch (SQLException e){
            throw  new RuntimeException(e);
        }
    }
}
