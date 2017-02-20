package com.congxiaoyao.exception.handler;

import com.congxiaoyao.auth.exception.InvalidTokenException;
import com.congxiaoyao.auth.exception.NoAuthenticatedInfoException;
import com.congxiaoyao.auth.exception.NotAuthenticatedException;
import com.congxiaoyao.common.pojo.ErrorInfo;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * 全局异常处理器
 * Created by Jaycejia on 2016/12/3.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 处理未授权的回应
     * HTTP状态码  401 UNAUTHORIZED
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ErrorInfo resolveUnauthorized(UnauthorizedException e) {
        return new ErrorInfo(ErrorInfo.AUTHORIZATION_ERROR, "无相关访问权限", e);
    }

    /**
     * 处理用户名或密码错误的回应
     * HTTP状态码  403 FORBIDDEN
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(IncorrectCredentialsException.class)
    public ErrorInfo resolveInvalidPassword(IncorrectCredentialsException e) {
        return new ErrorInfo(ErrorInfo.AUTHENTICATION_ERROR, "用户名或密码错误", e);
    }

    /**
     * 处理Token信息无效的回应
     * HTTP状态码  403 FORBIDDEN
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(InvalidTokenException.class)
    public ErrorInfo resolveInvalidToken(InvalidTokenException e) {
        return new ErrorInfo(ErrorInfo.AUTHENTICATION_ERROR, "Token信息无效", e);
    }

    /**
     * 处理Token超时失效的回应
     * HTTP状态码  403 FORBIDDEN
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ExpiredCredentialsException.class)
    public ErrorInfo resolveTokenExpired(ExpiredCredentialsException e) {
        return new ErrorInfo(ErrorInfo.AUTHENTICATION_ERROR, "认证超时，请重新登录", e);
    }

    /**
     * 处理未认证的回应
     * HTTP状态吗 403 FORBIDDEN
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(NotAuthenticatedException.class)
    public ErrorInfo resolveNotAuthenticated(NotAuthenticatedException e) {
        return new ErrorInfo(ErrorInfo.AUTHENTICATION_ERROR, "用户终端非法或未进行认证，请登录", e);
    }

    /**
     * 处理未认证的回应
     * HTTP状态吗 403 FORBIDDEN
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(NoAuthenticatedInfoException.class)
    public ErrorInfo resolveNoAuthenticatedInfo(NoAuthenticatedInfoException e) {
        return new ErrorInfo(ErrorInfo.AUTHENTICATION_ERROR, "未认证，请登录", e);
    }

    /**
     * 处理用户未找到的回应
     * HTTP状态码：404 NOT FOUND
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UnknownAccountException.class)
    public ErrorInfo resolveUnknownAccount(UnknownAccountException e) {
        return new ErrorInfo(ErrorInfo.AUTHENTICATION_ERROR, "找不到该用户", e);
    }

    /**
     * 处理未知错误
     * HTTP状态码：500 INTERNAL_SERVER_ERROR
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo resolveUnknownException(Exception e) {
        String errorMessage = "未知错误";
        if (logger.isErrorEnabled()) {
            logger.error(errorMessage, e);
        }
        return new ErrorInfo(ErrorInfo.UNKNOWN, errorMessage, e);
    }

    /**
     * 处理mybatis内部异常
     * HTTP状态码：500 INTERNAL_SERVER_ERROR
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({MyBatisSystemException.class, SQLException.class})
    public ErrorInfo resolveMybatisException(MyBatisSystemException e) {
        String errorMessage = "数据库操作错误";
        if (logger.isErrorEnabled()) {
            logger.error(errorMessage, e);
        }
        return new ErrorInfo(ErrorInfo.DAO_ERROR, errorMessage, e);
    }
}
