package com.ahu.achievement.user;

import java.util.List;

public interface UserService {
    User findByUsernameAndPassword(String username, String password);
    List<User> findAll();
    void InsertUser(User user);
}