package org.unibl.etf.forumback.services;

import org.unibl.etf.forumback.models.entities.ForumCategoryEntity;

import java.util.List;

public interface ForumCategoryService {

    List<ForumCategoryEntity> getAll();
}
