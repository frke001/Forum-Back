package org.unibl.etf.forumback.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.forumback.models.entities.ForumCategoryEntity;
import org.unibl.etf.forumback.repositories.ForumCategoryRepository;
import org.unibl.etf.forumback.services.ForumCategoryService;

import java.util.List;
@Service
public class ForumCategoryServiceImpl implements ForumCategoryService {

    private final ForumCategoryRepository forumCategoryRepository;

    public ForumCategoryServiceImpl(ForumCategoryRepository forumCategoryRepository) {
        this.forumCategoryRepository = forumCategoryRepository;
    }

    @Override
    public List<ForumCategoryEntity> getAll() {
        return forumCategoryRepository.findAll();
    }
}
