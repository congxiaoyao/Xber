package com.congxiaoyao.auth.filter;

import com.congxiaoyao.auth.exception.InvalidTokenException;
import com.congxiaoyao.auth.exception.NoAuthenticatedInfoException;
import com.congxiaoyao.auth.token.AuthenticateInfo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Jayce on 2016/9/22.
 * 登录认证过滤器
 */
public class LoginAuthcFilter extends AccessControlFilter {
    private Logger logger = LoggerFactory.getLogger(LoginAuthcFilter.class);
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        //1、根据客户端认证信息(Body中)生成认证信息Token
        String authcInfo = getRequestBody(request);
        AuthenticateInfo token = null;
        if (authcInfo == null || "".equals(authcInfo)) {
            onLoginFail(request, new NoAuthenticatedInfoException()); //缺少认证信息
            return true;
        } else {
            try {
                token = mapper.readValue(authcInfo, AuthenticateInfo.class);
            } catch (JsonParseException e) {
                onLoginFail(request,new InvalidTokenException());
            }
        }
        //2、委托给Realm进行登录
        try {
            getSubject(request, response).login(token);
            request.setAttribute("authcInfo",token);
        } catch (Exception e) {
            onLoginFail(request, e); //3、登录失败
        } finally {
            return true;
        }
    }


    //登录失败时将异常信息放入request attribute统一处理
    private void onLoginFail(ServletRequest request, Exception exp) throws IOException {
        if (logger.isTraceEnabled()) {
            logger.trace("登录失败：", exp);
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        httpRequest.setAttribute("shiroLoginFailure", exp);
    }

    //获取HttpServletRequest Body部分数据
    private String getRequestBody(ServletRequest request) throws IOException {
        Scanner s = new Scanner(request.getInputStream(), "UTF-8").useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
