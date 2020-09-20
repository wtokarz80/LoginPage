package com.wojtek.loginpage.dao;

import java.util.List;

public interface GenericDAO<T> {
    T read(int id);
    List<T> getAll();
}
