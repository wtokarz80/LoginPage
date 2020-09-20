package com.wojtek.loginpage.helpers;

import com.sun.net.httpserver.HttpExchange;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CookieHelper {

    private static final String SESSION_COOKIE_NAME = "sessionId";

    public List<HttpCookie> parseCookies(String cookieString){
        List<HttpCookie> cookies = new ArrayList<>();
        if(cookieString == null || cookieString.isEmpty()){ // what happens if cookieString = null?
            return cookies;
        }
        for(String cookie : cookieString.split(";")){
            int indexOfEq = cookie.indexOf('=');
            String cookieName = cookie.substring(0, indexOfEq);
            String cookieValue = cookie.substring(indexOfEq + 1, cookie.length());
            cookies.add(new HttpCookie(cookieName, cookieValue));
        }
        return cookies;
    }

    public Optional<HttpCookie> findCookieByName(String name, List<HttpCookie> cookies){
        for(HttpCookie cookie : cookies){
            if(cookie.getName().equals(name))
                return Optional.ofNullable(cookie);
        }
        return Optional.empty();
    }

//    public int changeToIntSessionId(HttpCookie cookie) {
//        String value = cookie.getValue().replace("\"", "");
//        return Integer.parseInt(value);
//    }

    public String getSessionIdFromCookie(HttpCookie cookie) {
        String value = cookie.getValue().replace("\"", "");
        return value;
    }

    public void createCookie(HttpExchange httpExchange, String sessionCookieName, String sessionId) {
        Optional<HttpCookie> cookie;
        cookie = Optional.of(new HttpCookie(sessionCookieName, sessionId));
        cookie.get().setPath("/login/");
        String cookieString = cookie.get().toString();
        httpExchange.getResponseHeaders().add("Set-Cookie", cookieString);



    }

    public String findSessionFromCookie(List<HttpCookie> cookies) {
        for (HttpCookie cookie : cookies) {
            if (cookie.getName().equals(SESSION_COOKIE_NAME)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public int parseSessionIdStringToInt(String sessionIdString) {
        String value = sessionIdString.replace("\"", "");
        return Integer.parseInt(value);
    }

    public Optional<HttpCookie> getSessionIdCookie(HttpExchange httpExchange){
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        List<HttpCookie> cookies = parseCookies(cookieStr);
        return findCookieByName(SESSION_COOKIE_NAME, cookies);
    }
}