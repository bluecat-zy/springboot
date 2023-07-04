package com.example.springboot3.exception;

public class SystemException extends BaseException {
    public SystemException(Integer code, String message) {
        super(code, message);
    }

}
