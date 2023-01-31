package org.aissms.cicada.controller;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String getHomePage(HttpSession principal) {
        if(principal != null && principal.getAttribute("login") != null) {
            return "home";
        }
        return "redirect:auth";
    }
}
