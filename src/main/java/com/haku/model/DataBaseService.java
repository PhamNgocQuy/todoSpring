package com.haku.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by QuyPN on 6/22/2017.
 */
public class DataBaseService {

    private static Connection connection;

    public static Connection getConnection() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tododb", "root", "buiquangcuong");
            } catch (SQLException e) {
                System.out.println("Connect Failed");
                e.printStackTrace();
            }
        }
        return connection;
    }
}
