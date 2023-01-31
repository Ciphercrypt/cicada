package org.aissms.cicada.messaging;

import java.util.Map;

import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

// Drop subscribe frames if channel is invalid

@Component
public class FrameInterceptor implements ChannelInterceptor {

    @Override
    @Nullable
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        if(StompCommand.SUBSCRIBE.compareTo(command) == 0) {
            Map<String,Object> map = accessor.getSessionAttributes();
            // OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) accessor.getUser();
            if(map == null) return null;
            String username = (String)map.get("login");
            String destination = "/messages/" + username;
            if(!destination.equalsIgnoreCase(accessor.getDestination())) {
                return null;
            }
        }
        return message;
    }
    
}
