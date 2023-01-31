package org.aissms.cicada.messaging;

import javax.servlet.http.HttpSession;

import org.aissms.cicada.entity.TextMessage;
import org.aissms.cicada.repository.TextMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;

// bug : can send message without friendship

@Controller
public class MessageController {
    
    @Autowired private SimpMessagingTemplate template;
    @Autowired private TextMessageRepository repository;
    @MessageMapping("/send")
    public void sendMessage(Message<TextMessage> message, HttpSession user) {
        TextMessage ms = message.getPayload();
        String name = (String)user.getAttribute("login");
        if(name != null && name.equalsIgnoreCase(ms.getSender())) {
            repository.save(ms);
            template.convertAndSend("/messages/" + ms.getRecver(), ms);
        }
    }
}
