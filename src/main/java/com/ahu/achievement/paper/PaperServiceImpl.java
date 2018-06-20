package com.ahu.achievement.paper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("PaperService")
public class PaperServiceImpl implements PaperService {
    @Autowired
    private PaperRepository paperRepository;

    @Override
    public List<Paper> findAll() {
        return paperRepository.findAll();
    }

    @Override
    public List<Paper> findByAuthorsid(String authorsid) {
        return paperRepository.findByAuthorsid(authorsid);
    }

    @Override
    public Paper findById(String id) {
        return paperRepository.findById(id);
    }

    @Override
    @Transactional
    public Paper savePaper(Paper paper) {
        Paper findPaper = paperRepository.findByAuthorsidAndPapername(paper.getAuthorsid(), paper.getPapername());
        if (findPaper != null)
            paperRepository.delete(findPaper);
        return paperRepository.save(paper);
    }


}
