package com.congxiaoyao.auth.exception;


import com.congxiaoyao.exception.CustomizedException;

/**
 * Created by Jayce on 2016/10/8.
 */
public class UserHasLogoutException extends LogOutException implements CustomizedException {
    public UserHasLogoutException() {
        super("用户已注销");
    }
}
