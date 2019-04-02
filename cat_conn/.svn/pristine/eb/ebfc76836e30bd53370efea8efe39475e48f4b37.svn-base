package com.cgltech.cat_conn.exception;

public class ServiceException extends RuntimeException{
    private String errorCode;

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(String errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    public ServiceException(String msg, Throwable exception) {
        super(msg, exception);
    }

    public ServiceException(String errorCode, String msg, Throwable exception) {
        super(msg, exception);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}