package com.wojtek.loginpage.dao;

import com.wojtek.loginpage.model.User;

import java.sql.SQLException;

public interface UserDAO extends GenericDAO<User> {

    User getUserByLoginPassword(String login, String password) throws SQLException;
}
