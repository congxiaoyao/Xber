package com.congxiaoyao.websocket.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Jaycejia on 2017/2/10.
 */
@RestController
public class WebSocketController {
    private final Logger logger = LoggerFactory.getLogger(WebSocketController.class);
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/hello")
    @SendTo("/topic/echo")
    public String greetings(String message) {
        logger.info("received: " + message);
        return message;
    }

    @RequestMapping(value = "/sendTest", method = RequestMethod.GET)
    public void sendTest(String content) {
        logger.info("invoked: " + content);
        simpMessagingTemplate.convertAndSend("/topic/echo", content);
        simpMessagingTemplate.convertAndSend("/topic/echo1", content);
        simpMessagingTemplate.convertAndSend("/topic/echo2", content);
        simpMessagingTemplate.convertAndSend("/topic/echo3", content);
    }
}
