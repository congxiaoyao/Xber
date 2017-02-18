package com.congxiaoyao.auth.filter;


import com.congxiaoyao.auth.exception.InvalidTokenException;
import com.congxiaoyao.auth.exception.NoAuthenticatedInfoException;
import com.congxiaoyao.auth.pojo.StatelessSession;
import com.congxiaoyao.auth.service.def.SessionService;
import com.congxiaoyao.auth.token.StatelessToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Jayce on 2016/9/21.
 * 身份认证过滤器
 */
public class StatelessAuthcFilter extends AccessControlFilter {
    private Logger logger = LoggerFactory.getLogger(StatelessAuthcFilter.class);
    @Autowired
    private SessionService sessionService;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //1、根据客户端认证信息生成无状态Token
        String requestMessage = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        try {
            StatelessToken token = checkToken(requestMessage);
            StatelessSession session = sessionService.getSessionByClientId((String) token.getPrincipal());
            request.setAttribute("userId", session.getUserId());
        } catch (Exception e) {
            onAuthcFail(request, e);
        } finally {
            return true;
        }
    }

    private void onAuthcFail(ServletRequest request, Exception exp) {
        if (logger.isTraceEnabled()) {
            logger.trace(exp.getMessage(), exp);
        }
        request.setAttribute("shiroLoginFailure", exp);
    }

    public static StatelessToken checkToken(String requestMessage) {
        StatelessToken token = new StatelessToken();
        if (requestMessage == null || "".equals(requestMessage)) {
            throw new NoAuthenticatedInfoException();//无认证，登录失败
        } else if (!requestMessage.matches("^Basic \\S+:\\S+$")) {
            throw new InvalidTokenException();
        } else {
            String clientId = requestMessage.replace("Basic ", "").split(":")[0];
            String tokenString = requestMessage.replace("Basic ", "").split(":")[1];
            token.setClientId(clientId);
            token.setAccessTokenString(tokenString);
            //委托认证
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
        }
        return token;
    }

}