package com.wojtek.loginpage;

import com.sun.net.httpserver.HttpServer;
import com.wojtek.loginpage.controller.LoginController;
import com.wojtek.loginpage.controller.LogoutController;
import com.wojtek.loginpage.dao.UserDAOImpl;
import com.wojtek.loginpage.model.User;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) throws IOException {

        // create a server on port 8001
        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);

        // set routes
        server.createContext("/login", new LoginController());
        server.createContext("/logout", new LogoutController());
        server.createContext("/static", new Static());
        server.setExecutor(null); // creates a default executor

        server.start();

    }
}
