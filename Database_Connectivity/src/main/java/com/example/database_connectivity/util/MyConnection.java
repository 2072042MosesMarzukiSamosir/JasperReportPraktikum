package com.example.database_connectivity.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    public static Connection getConnection() {
        Connection conn;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/databaseconnection_pt",
                    "root",
                    ""
            );
        } catch (ClassNotFoundException | SQLException e) {

            throw new RuntimeException(e);
        }
        return conn;
    }
}
