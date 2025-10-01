package com.blocksim.infrastructure.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DatabaseConfig {
    private static final Logger logger = LoggerConfig.getLogger(DatabaseConfig.class);

    private static DatabaseConfig instance;
    private Connection connection;

    private static final String DB_URL =
            "jdbc:mysql://" + System.getenv("DB_HOST") + ":" +
                    System.getenv("DB_PORT") + "/" +
                    System.getenv("MYSQL_DATABASE") +
                    "?allowPublicKeyRetrieval=true&useSSL=false";

    private static final String DB_USER = System.getenv("MYSQL_USER");
    private static final String DB_PASSWORD = System.getenv("MYSQL_PASSWORD");

    private DatabaseConfig() {
        try {
            LoggerConfig.init();
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException("Failed to initialize DB connection", e);
        }
    }

    public static DatabaseConfig getInstance() {
        if (instance == null) {
            synchronized (DatabaseConfig.class) {
                if (instance == null) {
                    instance = new DatabaseConfig();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException("Failed to reconnect to DB", e);
        }

        return connection;
    }
}
