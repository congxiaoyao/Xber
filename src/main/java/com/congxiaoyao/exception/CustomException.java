package com.congxiaoyao.exception;

/**
 * Created by Jaycejia on 2016/12/3.
 */
public class CustomException extends Exception implements CustomizedException{
    public CustomException(String message) {
        super(message);
    }
}
