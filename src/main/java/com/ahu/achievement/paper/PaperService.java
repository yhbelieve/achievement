package com.ahu.achievement.paper;

import org.springframework.stereotype.Service;

import java.util.List;

public interface PaperService {
    List<Paper> findAll();
    List<Paper> findByAuthorsid(String authorsid);
    Paper findById(String id);
    Paper savePaper(Paper paper);//插入数据和更新数据
}
