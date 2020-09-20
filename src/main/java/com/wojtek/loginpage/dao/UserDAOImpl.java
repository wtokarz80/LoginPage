package com.wojtek.loginpage.dao;

import com.wojtek.loginpage.model.User;
import com.wojtek.loginpage.connect.Connector;
//import org.graalvm.compiler.lir.LIRInstruction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    Connector connectionProvider = new Connector();

    @Override
    public User getUserByLoginPassword(String username, String password) {
        String query = String.format("SELECT * FROM users WHERE username='%s' AND password='%s';",username, password);

        return getUserFromDB(query);
    }

    private User getUserFromDB(String query) {
        User user = User.NOT_FOUND_USER;
        try {
            connectionProvider.connect();
            ResultSet rs = connectionProvider.getStatement().executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                user = new User(id, username, password);
            }
            rs.close();
            connectionProvider.getStatement().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }


    @Override
    public User read(int id) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }
}
