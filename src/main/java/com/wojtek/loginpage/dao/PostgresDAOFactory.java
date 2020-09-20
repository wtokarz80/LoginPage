package com.wojtek.loginpage.dao;

public class PostgresDAOFactory extends DAOFactory {

    @Override
    public UserDAO getUserDAO() {

        return new UserDAOImpl();
    }
}
