package org.aissms.cicada.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String username;
    private String imageLink;
    
    public User() {
    }

    public User(String username, String avatar) {
        this.username = username;
        this.imageLink = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String avatarUrl) {
        this.imageLink = avatarUrl;
    }
    
}
