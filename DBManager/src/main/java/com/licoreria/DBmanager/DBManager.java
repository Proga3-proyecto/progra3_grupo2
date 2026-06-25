package com.licoreria.DBmanager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class DBManager {

    private static DBManager instance;
    private final HikariDataSource dataSource;

    private static final String FILE_PATH = "db.properties";

    private DBManager() {

        Properties properties = new Properties();

        try (InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream(FILE_PATH)) {

            if (inputStream == null) {
                throw new RuntimeException("No se encontró " + FILE_PATH);
            }

            properties.load(inputStream);

        } catch (IOException e) {
            throw new RuntimeException("Error cargando " + FILE_PATH, e);
        }

        String user = require(properties.getProperty("db.usuario"), "db.usuario");
        String password = require(properties.getProperty("db.password"), "db.password");
        String host = require(properties.getProperty("db.host"), "db.host");
        String port = require(properties.getProperty("db.puerto"), "db.puerto");
        String database = require(properties.getProperty("db.esquema"), "db.esquema");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database +
                "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);

        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        config.setIdleTimeout(300000);
        config.setMaxLifetime(1800000);
        config.setConnectionTimeout(30000);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        this.dataSource = new HikariDataSource(config);
    }

    public static DBManager getInstance() {
        if (instance == null)
            instance = new DBManager();
        return instance;
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo conexión del pool", e);
        }
    }

    public void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    private String require(String value, String key) {
        if (value == null || value.isBlank()) {
            throw new RuntimeException("Falta la propiedad: " + key);
        }
        return value;
    }
}