package edu.barishal.ghat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConfig {
    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/barishal_launch_ghat"
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private DatabaseConfig() {
    }

    public static Connection getConnection() throws SQLException {
        Properties properties = loadProperties();
        String url = setting(properties, "db.url", "DB_URL", DEFAULT_URL);
        String user = setting(properties, "db.user", "DB_USER", "root");
        String password = setting(properties, "db.password", "DB_PASSWORD", "");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException exception) {
            throw new SQLException(
                    "MySQL Connector/J was not found. Reload the Maven project and try again.",
                    exception);
        }
        return DriverManager.getConnection(url, user, password);
    }

    private static Properties loadProperties() throws SQLException {
        Properties properties = new Properties();
        Path configFile = Path.of("db.properties");
        if (Files.exists(configFile)) {
            try (InputStream input = Files.newInputStream(configFile)) {
                properties.load(input);
            } catch (IOException exception) {
                throw new SQLException("Could not read db.properties.", exception);
            }
        }
        return properties;
    }

    private static String setting(
            Properties properties, String propertyName, String environmentName, String fallback) {
        String environmentValue = System.getenv(environmentName);
        if (environmentValue != null && !environmentValue.isBlank()) {
            return environmentValue;
        }
        return properties.getProperty(propertyName, fallback);
    }
}
