package com.wmirs.archive.poeaa.patterns;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @Desc
 * @Author WangMu
 * @Create 2022-04-19
 */
public class PoeaaConnection {
    private static final String JDBC_URL = "jdbc:mysql://192.168.56.102:3306/poeaa";
    private static final String JDBC_URER = "root";
    private static final String JDBC_PWD = "root";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(JDBC_URL, JDBC_URER, JDBC_PWD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
