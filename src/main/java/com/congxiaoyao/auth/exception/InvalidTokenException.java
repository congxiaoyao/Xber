package com.congxiaoyao.auth.exception;

import com.congxiaoyao.exception.CustomizedException;
import org.apache.shiro.authc.AuthenticationException;

/**
 * Created by Jayce on 2016/9/29.
 */
public class InvalidTokenException extends AuthenticationException implements CustomizedException {
    public InvalidTokenException() {
        super("token无效");
    }

    public InvalidTokenException(Throwable cause) {
        super(cause);
    }
}
