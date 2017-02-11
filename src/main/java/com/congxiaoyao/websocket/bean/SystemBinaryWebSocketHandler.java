package com.congxiaoyao.websocket.bean;

import com.congxiaoyao.location.pojo.GpsSampleOuterClass;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

/**
 * Created by Jaycejia on 2016/12/11.
 */
public class SystemBinaryWebSocketHandler extends BinaryWebSocketHandler{
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        GpsSampleOuterClass.GpsSample gpsSample = GpsSampleOuterClass.GpsSample.parseFrom(message.getPayload().array());
        System.out.println(gpsSample);

    }
}
