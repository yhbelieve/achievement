package com.ahu.achievement.user;

import org.springframework.data.annotation.Id;

import javax.annotation.Generated;

public class User {
    @Id
    private String id;
//    username为学号
    private String username;
    private String password;
    private Integer level;
    private String nickname;



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public User() {
    }

    public User(String id, String username, String password, Integer level, String nickname) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.level = level;
        this.nickname = nickname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
