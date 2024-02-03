package org.unibl.etf.forumback.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.forumback.models.dto.PermissionDTO;
import org.unibl.etf.forumback.models.dto.RequestCommentDTO;
import org.unibl.etf.forumback.models.dto.ResponseCommentDTO;
import org.unibl.etf.forumback.models.entities.ForumCategoryEntity;
import org.unibl.etf.forumback.services.CommentService;
import org.unibl.etf.forumback.services.ForumCategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/forum-categories")
public class ForumCategoryController {

    private final ForumCategoryService forumCategoryService;
    private final CommentService commentService;

    public ForumCategoryController(ForumCategoryService forumCategoryService, CommentService commentService) {
        this.forumCategoryService = forumCategoryService;
        this.commentService = commentService;
    }

    @GetMapping
    public List<ForumCategoryEntity> getAll(){
        return forumCategoryService.getAll();
    }
    @GetMapping("/category/{id}")
    public ForumCategoryEntity getById(@PathVariable Long id){
        return forumCategoryService.getById(id);
    }
    @GetMapping("/{id}")
    List<ResponseCommentDTO> getAll(@PathVariable Long id){
        return commentService.getAll(id);
    }
    @PostMapping("/{id}")
    public void insertComment(@PathVariable Long id, @RequestBody @Valid RequestCommentDTO request, Authentication auth){
        commentService.insert(id,request,auth);
    }

    @PutMapping("/comments/{id}")
    public void updateComment(@PathVariable Long id, @RequestBody @Valid RequestCommentDTO request, Authentication auth){
        commentService.update(id,request,auth);
    }

    @DeleteMapping("/comments/{commentId}/{userId}")
    public void deleteComment(@PathVariable Long commentId, @PathVariable Long userId, Authentication auth){
        commentService.delete(commentId,userId,auth);
    }
}
