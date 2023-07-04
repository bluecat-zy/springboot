package com.example.springboot3.exception;

public class ConnectException extends BaseException {
    public ConnectException(Integer code, String message) {
        super(code, message);
    }
}
