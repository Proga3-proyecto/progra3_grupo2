package com.licoreria.DBmanager;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class DBManager {
    private final String user;
    private final String password;
    private final String url;
    private final String FILE_PATH = "db.properties";

    private static  DBManager instance;

    private DBManager() {
        Properties properties = new Properties();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(FILE_PATH);
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        user = properties.getProperty("db.usuario");
        password = properties.getProperty("db.password");
        String host = properties.getProperty("db.host");
        String port = properties.getProperty("db.puerto");
        String database = properties.getProperty("db.esquema");
        url = "jdbc:mysql://" + host + ":" + port + "/" + database +
                "?useSSL=false&allowPublicKeyRetrieval=true";
    }

    public Connection getConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url , user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static DBManager getInstance() {
        if(instance  == null){
            instance = new DBManager();
        }
        return instance;
    }
}
