package com.licoreria.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOUtils {

    @FunctionalInterface
    public interface SetSQLIdFunction<T> {
        void configure(PreparedStatement ps, T id) throws SQLException;
    }

    @FunctionalInterface
    public interface SetParamsFunction {
        void execute(PreparedStatement ps) throws SQLException;
    }


    @FunctionalInterface
    public interface MapFunction<T> {
        T execute(ResultSet res) throws SQLException;
    }

    @FunctionalInterface
    public interface MapKeyFunction {
        void execute(ResultSet res) throws SQLException;
    }


    @FunctionalInterface
    public interface SetIdFunction<T> {
        void execute(ResultSet res, T t) throws SQLException;
    }


    @FunctionalInterface
    public interface MapSQLFunction<T> {
        void execute(PreparedStatement res, T data) throws SQLException;
    }


    public static <T, U> T get(String sql, Connection con, U id, SetSQLIdFunction<U> setId, MapFunction<T> mapFunc) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            setId.configure(ps, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapFunc.execute(rs);
                }
            }
        }
        return null;
    }

    public static <T, U> T get(String sql, Connection con, SetParamsFunction setParams, MapFunction<T> mapFunc) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            setParams.execute(ps);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapFunc.execute(rs);
                }
            }
        }
        return null;
    }

    public static <T> List<T> getAll(String sql, Connection con, MapFunction<T> mapFunc) throws SQLException {
        ArrayList<T> list = new ArrayList<>();
        try (
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(mapFunc.execute(rs));
            }
        }
        return list;
    }

    public static <T> List<T> getAll(String sql, Connection con, SetParamsFunction setParams, MapFunction<T> mapFunc) throws SQLException {
        ArrayList<T> list = new ArrayList<>();
        try (
                PreparedStatement ps = con.prepareStatement(sql);

        ) {
            setParams.execute(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapFunc.execute(rs));
                }

            }

        }
        return list;
    }


    public static <T, U> void delete(String sql, Connection con, U id, SetSQLIdFunction<U> setId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            setId.configure(ps, id);
            ps.executeUpdate();
        }
    }

    public static <T, U> void delete(String sql, Connection con, SetParamsFunction setParams) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            setParams.execute(ps);
            ps.executeUpdate();
        }
    }

    public static <T, U> void update(String sql, Connection con, T entity, MapSQLFunction<T> mapFunc) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            mapFunc.execute(ps, entity);
            ps.executeUpdate();
        }
    }

    public static <T, U> void update(String sql, Connection con, SetParamsFunction mapFunc) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            mapFunc.execute(ps);
            ps.executeUpdate();
        }
    }


    public static <T> void save(String sql, Connection con, T entity, MapSQLFunction<T> mapFunc, SetIdFunction<T> setidFunc) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            mapFunc.execute(ps, entity);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys();) {
                if (rs.next()) {
                    setidFunc.execute(rs, entity);
                }
            }
        }
    }

    public static <T> void save(String sql, Connection con, SetParamsFunction mapFunc, MapKeyFunction setidFunc) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            mapFunc.execute(ps);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys();) {
                if (rs.next()) {
                    setidFunc.execute(rs);
                }
            }
        }
    }
}
