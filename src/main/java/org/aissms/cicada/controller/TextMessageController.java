package org.aissms.cicada.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.aissms.cicada.entity.TextMessage;
import org.aissms.cicada.repository.TextMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TextMessageController {
    @Autowired TextMessageRepository repository;

    @GetMapping("/text/{username}")
    public List<TextMessage> getTextMessage(@PathVariable String username, HttpSession oauth) {
        List<TextMessage> res = repository.findAllBySenderAndRecver(username,(String) oauth.getAttribute("login"));
        res.addAll(repository.findAllBySenderAndRecver((String)oauth.getAttribute("login"), username));
        Collections.sort(res, (text1, text2) -> {
            return text1.getId() < text2.getId() ? -1 : 1;
        });
        return res;
    }
}
