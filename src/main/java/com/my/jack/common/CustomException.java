package com.my.jack.common;

/*
 * 自定义异常
 * */
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
