package com.wojtek.loginpage.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Connector {

    public Connection connection;
    private Statement statement;

    private final String url = System.getenv("url");
    private final String user = System.getenv("user");
    private final String password = System.getenv("password");


    public Statement getStatement() {
        return statement;
    }

    public void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to DB");
            statement = connection.createStatement();
        } catch(SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Not connected");
        }
    }
}
