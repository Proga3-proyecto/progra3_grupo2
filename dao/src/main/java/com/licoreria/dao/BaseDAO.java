package com.licoreria.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BaseDAO<T, ID> {

    T get(Connection con, ID id) throws SQLException;

    List<T> getAll(Connection con) throws SQLException;

    T save(Connection con, T t) throws SQLException;

    T update(Connection con, T t) throws SQLException;

    void remove(Connection con, T t) throws SQLException;
}
