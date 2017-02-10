package com.congxiaoyao.websocket.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * Created by Jaycejia on 2016/12/11.
 */
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        logger.info(request.getRemoteAddress() + " connecting;headers" + request.getHeaders());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        logger.info(request.getRemoteAddress() + " connected.");
    }
}
