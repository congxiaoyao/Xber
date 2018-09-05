package com.congxiaoyao.websocket.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    private final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic","/user");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket-handshake")
                .addInterceptors(new WebSocketHandshakeInterceptor())
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        super.configureClientInboundChannel(registration);
        registration.setInterceptors(new ChannelInterceptorAdapter() {
            @Override
            public void afterSendCompletion(Message<?> message, MessageChannel channel,
                                            boolean sent, Exception ex) {
                MessageHeaders headers = message.getHeaders();
                Object o = headers.get("stompCommand");
                if (o == null) return;
                StompCommand command = (StompCommand) o;
                if (command == StompCommand.SUBSCRIBE) {
                    Map<String, LinkedList<String>> nativeHeaders = (Map<String,
                            LinkedList<String>>) headers.get("nativeHeaders");
                    String destination = nativeHeaders.get("destination").getFirst();
                    logger.info("subscribe path " + destination);
                    Map<String, Object> map = new TreeMap<>();
                    map.put("is_subscribe_callback", "true");
                    CompletableFuture.runAsync(() -> {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        logger.info("SUBSCRIBE RECEIPTED " + destination);
                        new SimpMessagingTemplate(channel).convertAndSend(destination,
                                "SUBSCRIBE RECEIPTED".getBytes(), map);
                    });
                }
            }
        });
    }
}