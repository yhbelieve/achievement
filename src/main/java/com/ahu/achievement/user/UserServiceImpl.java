package com.ahu.achievement.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username,password);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }



    @Override
    @Transactional
    public void InsertUser(User user) {
        userRepository.save(user);
    }
}
