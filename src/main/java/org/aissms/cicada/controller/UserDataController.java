package org.aissms.cicada.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserDataController {
    
    @GetMapping("/user/data")
    public OAuth2User userDataController(@AuthenticationPrincipal OAuth2User user) {
        return user;
    }
}
