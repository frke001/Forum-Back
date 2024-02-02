package org.unibl.etf.forumback.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.forumback.models.dto.PermissionDTO;
import org.unibl.etf.forumback.models.entities.ForumCategoryEntity;
import org.unibl.etf.forumback.services.ForumCategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/forum-categories")
public class ForumCategoryController {

    private final ForumCategoryService forumCategoryService;

    public ForumCategoryController(ForumCategoryService forumCategoryService) {
        this.forumCategoryService = forumCategoryService;
    }

    @GetMapping
    public List<ForumCategoryEntity> getAll(){
        return forumCategoryService.getAll();
    }
}
