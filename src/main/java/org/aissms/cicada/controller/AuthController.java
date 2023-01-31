package org.aissms.cicada.controller;

import javax.servlet.http.HttpSession;

import org.aissms.cicada.entity.User;
import org.aissms.cicada.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthController {
    @Autowired
    UserRepository repository;
    @GetMapping("/auth")
    @ResponseBody
    public String getAuthPage(@AuthenticationPrincipal OAuth2User principal) {
        if(principal != null && principal.getAttribute("login") != null) {
            return "register first by post method to register/username";
        }
        return "auth";
    }
    @PostMapping("/register/{username}")
    @ResponseBody
    public String register(HttpSession session, @PathVariable String username) {
        if(session.getAttribute("login") != null) return "null";
        session.setAttribute("login", username);
        User user = new User(username, "https://avatars.githubusercontent.com/u/122953429?v=4");
        User exist = repository.findByUsername(username);
        if(exist == null) {
            repository.save(user);
        }
        repository.save(user);
        return username;
    }

    @GetMapping("/public/data/{username}")
    @ResponseBody
    public User getUserDataPublic(@PathVariable String username) {
        return repository.findByUsername(username);
    }
}
