package com.wojtek.loginpage.service;

import com.wojtek.loginpage.dao.UserDAOImpl;
import com.wojtek.loginpage.model.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserService {

    private UserDAOImpl userDAO = new UserDAOImpl();
    private final Map<Integer, User> usersSessionIdMap = new HashMap<>();

    public UserService() {
    }


    public Map<Integer, User> getUsersSessionIdMap(){
        return usersSessionIdMap;
    }

    public User getUserBySessionId(int sessionId){
        return usersSessionIdMap.get(sessionId);
    }

    public User getUser(Map<String, String> inputs) throws SQLException {
        String userName = inputs.get("user-name");
        String password = inputs.get("password");
        // TO DO
        // validate userName and password before passing them to dao
        return userDAO.getUserByLoginPassword(userName, password);

    }



    public void deleteUser(Integer sessionId){
        usersSessionIdMap.remove(sessionId);
    }
}
