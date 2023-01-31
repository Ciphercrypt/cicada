package org.aissms.cicada.controller;

import java.util.List;

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
    public void sendInvitation(@PathVariable String username,HttpSession user) {
        Invitation invite = new Invitation((String)user.getAttribute("login"), username);
        inviteRepository.save(invite);
    }

    @PostMapping("/accept/{username}")
    @Transactional
    public void acceptInvitation(@PathVariable String username,HttpSession user) {
        String currentUser = (String) user.getAttribute("login");
        Invitation invite = inviteRepository.findByInviteFromAndInviteTo(username, currentUser);
        if(invite == null) return;
        inviteRepository.deleteByInviteFromAndInviteTo(username, currentUser);
        Friendship friendship = new Friendship(username, currentUser);
        friendRepository.save(friendship);
    }

    @GetMapping("/all")
    public List<Invitation> getAllInvitation(HttpSession user) {
        List<Invitation> list = inviteRepository.findByInviteFrom((String)user.getAttribute("login"));
        list.addAll(inviteRepository.findByInviteTo((String)user.getAttribute("login")));
        return list;
    }
}
