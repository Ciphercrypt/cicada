package org.aissms.cicada.events;

import org.aissms.cicada.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {
    
    @Autowired UserStatusRepository repository;

    @EventListener
    public void handleSessionConnectedEvent(SessionConnectedEvent event) {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) event.getUser();
        if(token == null) return;
        String name = token.getPrincipal().getAttribute("login");
        if(name != null) {
            System.out.println("Connected : " + name);
            repository.setOnline(name);
        }
    }

    @EventListener
    public void handleSessionDisconnectedEvent(SessionDisconnectEvent event) {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) event.getUser();
        if(token == null) return;
        String name = token.getPrincipal().getAttribute("login");
        if(name != null) {
            System.out.println("Disconnected : " + name);
            repository.setOffline(name);
        }
    }
}
