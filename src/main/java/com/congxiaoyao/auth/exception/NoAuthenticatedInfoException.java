package com.congxiaoyao.auth.exception;

import com.congxiaoyao.exception.CustomizedException;
import org.apache.shiro.authc.AuthenticationException;

/**
 * Created by Jayce on 2016/9/22.
 */
public class NoAuthenticatedInfoException extends AuthenticationException implements CustomizedException {
    public NoAuthenticatedInfoException() {
        super("未认证，请登录");
    }
}
