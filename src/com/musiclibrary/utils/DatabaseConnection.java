package com.musiclibrary.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private static final String URL = "jdbc:sqlite:music_library.db";

    private DatabaseConnection() {
        try {
            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection(URL);
            System.out.println("✓ SQLite Database connected! File: music_library.db");

            initializeDatabase();
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite Driver Error: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("SQLite Error: " + e.getMessage());
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void initializeDatabase() {
    }

    public void closeResources(ResultSet rs, PreparedStatement ps) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}
