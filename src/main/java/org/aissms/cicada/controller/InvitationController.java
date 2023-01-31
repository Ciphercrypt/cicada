package org.aissms.cicada.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.aissms.cicada.entity.Friendship;
import org.aissms.cicada.entity.Invitation;
import org.aissms.cicada.repository.FriendshipRepository;
import org.aissms.cicada.repository.InvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/invite")
public class InvitationController {
    
    @Autowired InvitationRepository inviteRepository;
    @Autowired FriendshipRepository friendRepository;

    @PostMapping("/send/{username}")
    public void sendInvitation(@PathVariable String username,HttpSession user, HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");
        System.out.println(authorizationHeader);

        Invitation invite = new Invitation((String)authorizationHeader, username);
        inviteRepository.save(invite);
    }

    @PostMapping("/accept/{username}")
    @Transactional
    public void acceptInvitation(@PathVariable String username,HttpSession user,HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");
        System.out.println(authorizationHeader);


       // String currentUser = (String) user.getAttribute("login");
        Invitation invite = inviteRepository.findByInviteFromAndInviteTo(username, authorizationHeader);
        if(invite == null) return;
        inviteRepository.deleteByInviteFromAndInviteTo(username, authorizationHeader);
        Friendship friendship = new Friendship(username, authorizationHeader);
        friendRepository.save(friendship);
    }

    @GetMapping("/all")
    public List<Invitation> getAllInvitation(HttpSession user, HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");
        System.out.println(authorizationHeader);

        List<Invitation> list = inviteRepository.findByInviteFrom(authorizationHeader)
        System.out.println(inviteRepository);
        list.addAll(inviteRepository.findByInviteTo(authorizationHeader));
        return list;
    }
}
