package com.ahu.achievement.user;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Long> {
    User findByUsernameAndPassword(String username, String password);
}
