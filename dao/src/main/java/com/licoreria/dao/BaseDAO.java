package com.licoreria.dao;

import java.sql.SQLException;
import java.util.List;

public interface BaseDAO <T, ID> {
    T get(ID id) throws SQLException;
    List<T> getAll() throws  SQLException;
    T save(T t) throws SQLException;
    T update(T t) throws SQLException;
    void remove(T t) throws SQLException;
}
