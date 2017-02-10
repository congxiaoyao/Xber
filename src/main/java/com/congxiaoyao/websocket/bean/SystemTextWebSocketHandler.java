package com.congxiaoyao.websocket.bean;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

/**
 * Created by Jaycejia on 2016/12/11.
 */
public class SystemTextWebSocketHandler extends BinaryWebSocketHandler{
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        super.handleBinaryMessage(session, message);
    }
}
