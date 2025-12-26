package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Db {

    private static final String HOST =
            System.getenv().getOrDefault("DB_HOST", "localhost");

    private static final String PORT =
            System.getenv().getOrDefault("DB_PORT", "3306");

    private static final String DB_NAME =
            System.getenv().getOrDefault("DB_NAME", "LampreaDB");

    private static final String USER =
            System.getenv().getOrDefault("DB_USER", "root");

    private static final String PASS =
            System.getenv().getOrDefault("DB_PASS", "35560712dg");

    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME +
                    "?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";

    private Db() {}

    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", USER);
        props.setProperty("password", PASS);
        return DriverManager.getConnection(URL, props);
    }
}
