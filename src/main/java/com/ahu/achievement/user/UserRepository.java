package com.ahu.achievement.user;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, Long> {
    User findByUsernameAndPassword(String username, String password);
    User findById(String id);
    List<User> findByUsername(String username);
}
