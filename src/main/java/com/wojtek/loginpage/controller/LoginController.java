package com.wojtek.loginpage.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.wojtek.loginpage.helpers.CookieHelper;
import com.wojtek.loginpage.helpers.LoginHelper;
import com.wojtek.loginpage.model.User;
import com.wojtek.loginpage.service.UserService;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

public class LoginController  implements HttpHandler {

    private static final String SESSION_COOKIE_NAME = "sessionId";
    private static int counter = 0;

    private final UserService userService = new UserService();
    private final CookieHelper cookieHelper = new CookieHelper();
    private final LoginHelper loginHelper = new LoginHelper();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();

        if(method.equals("GET")){
            initializeGet(httpExchange);
        } else if (method.equals("POST")){
            try {
                initializeLogin(httpExchange);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void initializeGet(HttpExchange httpExchange) throws IOException {

        Optional<HttpCookie> cookie = cookieHelper.getSessionIdCookie(httpExchange);
        if (cookie.isPresent() && isSessionNotEmpty(cookie)) {
            int sessionId = Integer.parseInt(cookieHelper.getSessionIdFromCookie(cookie.get()));
            User user = userService.getUserBySessionId(sessionId);
            greetUser(user, httpExchange);
        } else {
            initializeLoginPage(httpExchange);
        }
    }

    public boolean isSessionNotEmpty(Optional<HttpCookie> cookie){
        return  !cookieHelper.getSessionIdFromCookie(cookie.get()).equals("");
    }

    private void initializeLogin(HttpExchange httpExchange) throws IOException, SQLException {

        String formData = getStringData(httpExchange);
        Map<String,String> inputs = LoginHelper.parseFormData(formData);
        String sessionId = String.valueOf(counter);
        cookieHelper.createCookie(httpExchange, SESSION_COOKIE_NAME, sessionId);
        User user = userService.getUser(inputs);
        if(user.getId() == 0){ initializeLoginPage(httpExchange); }
        if(!userService.getUsersSessionIdMap().containsValue(user)){
            userService.getUsersSessionIdMap().put(counter++, user);
            System.out.println(userService.getUsersSessionIdMap());
        }
        greetUser(user, httpExchange);
    }

    private String getStringData(HttpExchange httpExchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        return br.readLine();
    }

    private void initializeLoginPage(HttpExchange httpExchange) throws IOException {
        String response = "";
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/loginpageTemplate.twig");
        JtwigModel model = JtwigModel.newModel();
        response = template.render(model);
        loginHelper.sendResponse(httpExchange, response);
    }

    private void greetUser(User user, HttpExchange httpExchange) throws IOException {
        String response = "";
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/logoutTemplate.twig");
        JtwigModel model = JtwigModel.newModel();
        model.with("user", user);
        response = template.render(model);
        loginHelper.sendResponse(httpExchange, response);
    }



}
