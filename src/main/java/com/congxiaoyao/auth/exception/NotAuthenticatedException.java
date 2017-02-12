package com.congxiaoyao.auth.exception;

import com.congxiaoyao.exception.CustomizedException;
import org.apache.shiro.authc.AuthenticationException;

/**
 * Created by Jayce on 2016/9/22.
 */
public class NotAuthenticatedException extends AuthenticationException implements CustomizedException {
    public NotAuthenticatedException() {
        super("用户终端非法或未进行认证，请登录");
    }
}
