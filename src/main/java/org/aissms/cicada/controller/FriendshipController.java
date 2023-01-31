package org.aissms.cicada.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
public List<Friendship> getAllFriends(@AuthenticationPrincipal OAuth2User oAuth2User, HttpServletRequest request) {
    // String user = oAuth2User.getAttribute("login");
    // System.out.println(user);

    String authorizationHeader = request.getHeader("Authorization");
    System.out.println(authorizationHeader);

    List<Friendship> friendList = friendshipRepository.findByUsername(authorizationHeader);
    System.out.println(friendshipRepository);
    friendList.addAll(friendshipRepository.findByFriend(authorizationHeader));
    return friendList;
}


    @PostMapping("/delete/{username}")
    @Transactional
    public void deleteFriendship(@PathVariable String username,HttpSession oAuth2User, HttpServletRequest request) {


        String authorizationHeader = request.getHeader("Authorization");
    System.out.println(authorizationHeader);
        //String currentUser = (String) oAuth2User.getAttribute("login");
        friendshipRepository.deleteByUsernameAndFriend(authorizationHeader, username);
        friendshipRepository.deleteByUsernameAndFriend(username, authorizationHeader);
    }
}
