package com.example.springboot3.common;

public class Result {
    private int code;
    private String message;
    private Object data;

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result success(String message) {
        return new Result(200, message);
    }

    public static Result success(Object data) {
        return new Result(200, data);
    }

    public static Result success(String message, Object data) {
        return new Result(200, message, data);
    }

    public static Result success(int code, String message) {
        return new Result(code, message);
    }

    public static Result success(int code, Object data) {
        return new Result(code, data);
    }

    public static Result error(String message) {
        return new Result(500, message);
    }

    public static Result error(Object data) {
        return new Result(500, data);
    }

    public static Result error(int code, String message) {
        return new Result(code, message);
    }

    public static Result error(int code, Object data) {
        return new Result(code, data);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
