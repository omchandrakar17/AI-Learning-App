package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:" +
            new java.io.File("ai_learning.db").getAbsolutePath();

    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.sqlite.JDBC");
                System.out.println("DB Path: " + new java.io.File("ai_learning.db").getAbsolutePath());
                connection = DriverManager.getConnection(URL);
                createTables(connection);
            }
            return connection;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("DB Connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTables(Connection conn) {
        try (Statement stmt = conn.createStatement()) {

            // USERS TABLE
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "username TEXT NOT NULL UNIQUE," +
                            "age INTEGER," +
                            "email TEXT UNIQUE," +
                            "password TEXT NOT NULL)"
            );

            // HISTORY TABLE
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS history (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "user_id INTEGER NOT NULL," +
                            "user_input TEXT," +
                            "ai_response TEXT," +
                            "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP," +
                            "FOREIGN KEY (user_id) REFERENCES users(id))"
            );

            // SAVED CONTENT TABLE
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS saved_content (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "user_id INTEGER NOT NULL," +
                            "title TEXT," +
                            "content TEXT," +
                            "saved_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                            "FOREIGN KEY (user_id) REFERENCES users(id))"
            );

            System.out.println("Tables ready!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}