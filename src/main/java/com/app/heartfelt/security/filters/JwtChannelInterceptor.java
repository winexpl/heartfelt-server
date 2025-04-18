package com.app.heartfelt.security.filters;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

public class JwtChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String token = accessor.getFirstNativeHeader("Authorization");

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            if (token == null || !token.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Не авторизован");
            }
        }

        return message;
    }
}
