package com.wojtek.loginpage.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.wojtek.loginpage.helpers.CookieHelper;
import com.wojtek.loginpage.helpers.LoginHelper;
import com.wojtek.loginpage.model.User;
import com.wojtek.loginpage.service.UserService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.util.List;

public class LogoutController implements HttpHandler {

    private static final String SESSION_COOKIE_NAME = "sessionId";
    private static int counter = 0;

    private final UserService userService = new UserService();
    private final CookieHelper cookieHelper = new CookieHelper();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        List<HttpCookie> cookies = cookieHelper.parseCookies(cookieStr);
        String sessionIdString = cookieHelper.findSessionFromCookie(cookies);
        int sessionId = cookieHelper.parseSessionIdStringToInt(sessionIdString);
        userService.deleteUser(sessionId);
        HttpCookie cookie = new HttpCookie(SESSION_COOKIE_NAME, "");
        cookie.setMaxAge(-1);
        httpExchange.getResponseHeaders().add("Cache-Control", "no-store");
        httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());
        httpExchange.getResponseHeaders().add("Location", "login");
        sendResponse(httpExchange, 301, "");
    }

    private void sendResponse(HttpExchange exchange, int rCode, String response) throws IOException {
        exchange.sendResponseHeaders(rCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
