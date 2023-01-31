package org.aissms.cicada.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.aissms.cicada.entity.Friendship;
import org.aissms.cicada.repository.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/friend")
public class FriendshipController {
    @Autowired FriendshipRepository friendshipRepository;

    @GetMapping("/all")
    public List<Friendship> getAllFriends(HttpSession oAuth2User) {
        String user = (String) oAuth2User.getAttribute("login");
        List<Friendship> friendList = friendshipRepository.findByUsername(user);
        friendList.addAll(friendshipRepository.findByFriend(user));
        return friendList;
    }

    @PostMapping("/delete/{username}")
    @Transactional
    public void deleteFriendship(@PathVariable String username,HttpSession oAuth2User) {
        String currentUser = (String) oAuth2User.getAttribute("login");
        friendshipRepository.deleteByUsernameAndFriend(currentUser, username);
        friendshipRepository.deleteByUsernameAndFriend(username, currentUser);
    }
}
