package com.licoreria.dao.utils;

import java.util.Date;

public class DateUtils {
    private DateUtils () { }

    public static java.sql.Date toSqlDate(Date date){
        if(date == null) return null;
        return new java.sql.Date(date.getTime());
    }

    public static java.sql.Date toDate(long time){
        return new java.sql.Date(time);
    }


    public static java.sql.Date toSqlDate(Date date, long defaultValue ){
        if(date == null) return new java.sql.Date(defaultValue);
        return new java.sql.Date(date.getTime());
    }
}
