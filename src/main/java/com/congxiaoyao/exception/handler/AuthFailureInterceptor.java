package com.congxiaoyao.exception.handler;

import org.apache.shiro.authc.AuthenticationException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证异常处理拦截器
 */
public class AuthFailureInterceptor implements HandlerInterceptor {
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handle,
                                Exception exception) throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handle,
                           ModelAndView modelAndView) throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle) throws Exception {
		AuthenticationException exception = (AuthenticationException) request.getAttribute("shiroLoginFailure");
		if (exception == null) {
			return true;
		} else {
			throw exception;
		}
	}

}


