package com.productmanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class DBConnection {

    public Connection getDBConnection() throws SQLException {
        Properties props = new Properties();

        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("database.properties")) {

            if (input == null) {
                throw new SQLException("database.properties not found");
            }

            props.load(input);

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.username");
            String password = props.getProperty("db.password");

            // Load driver
            Class.forName("org.postgresql.Driver");

            return DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            throw new SQLException("Failed to get database connection", e);
        }
    }
}