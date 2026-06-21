package com.licoreria.BusinessLayer;


public class DAOException extends BusinessException {
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
    public DAOException(String message) {
        super(message);
    }
}