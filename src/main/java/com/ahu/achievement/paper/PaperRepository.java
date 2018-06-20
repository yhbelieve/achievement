package com.ahu.achievement.paper;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PaperRepository extends MongoRepository<Paper,Long> {
    List<Paper> findAll();
    List<Paper> findByAuthorsid(String authorsid);
    Paper findByAuthorsidAndPapername(String authorsid ,String papername);
    Paper findById(String id);
    Paper save(Paper paper);
}
