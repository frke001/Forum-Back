package org.unibl.etf.forumback.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.unibl.etf.forumback.exceptions.NotFoundException;
import org.unibl.etf.forumback.exceptions.UnauthorizedException;
import org.unibl.etf.forumback.models.dto.JwtUserDTO;
import org.unibl.etf.forumback.models.dto.RequestCommentDTO;
import org.unibl.etf.forumback.models.dto.ResponseCommentDTO;
import org.unibl.etf.forumback.models.entities.CommentEntity;
import org.unibl.etf.forumback.models.entities.ForumCategoryEntity;
import org.unibl.etf.forumback.models.entities.UserEntity;
import org.unibl.etf.forumback.repositories.CommentRepository;
import org.unibl.etf.forumback.repositories.ForumCategoryRepository;
import org.unibl.etf.forumback.services.CommentService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ForumCategoryRepository forumCategoryRepository;
    private final ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepository, ModelMapper modelMapper, ForumCategoryRepository forumCategoryRepository) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.forumCategoryRepository = forumCategoryRepository;
    }

    @Override
    public List<ResponseCommentDTO> getAll(Long id) {
        if(!forumCategoryRepository.existsById(id)){
            throw new NotFoundException();
        }
        var list = commentRepository.findTop20ByCategoryOrderByCreationDateDesc(id,PageRequest.of(0,20));
        return list.stream().map(el->{
            ResponseCommentDTO responseCommentDTO = modelMapper.map(el, ResponseCommentDTO.class);
            responseCommentDTO.setCreationDate(new SimpleDateFormat("dd MMM, yyyy HH:mm").format(el.getCreationDate()));
            return responseCommentDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void insert(Long id, RequestCommentDTO request, Authentication auth) {
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(request.getUserSenderId()))
        {
            throw new UnauthorizedException();
        }
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(null);
        commentEntity.setText(request.getText());
        UserEntity userEntity = new UserEntity();
        userEntity.setId(request.getUserSenderId());
        commentEntity.setUserSender(userEntity);
        ForumCategoryEntity forumCategoryEntity = new ForumCategoryEntity();
        forumCategoryEntity.setId(id);
        commentEntity.setCategory(forumCategoryEntity);
        commentEntity.setApproved(false);
        commentEntity.setCreationDate(new Date());
        commentRepository.saveAndFlush(commentEntity);

    }

    @Override
    public void update(Long id, RequestCommentDTO request, Authentication auth) {
        var jwtUser =(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(request.getUserSenderId()))
        {
            throw new UnauthorizedException();
        }
        CommentEntity commentEntity = commentRepository.findById(id).orElseThrow(NotFoundException::new);
        commentEntity.setCreationDate(new Date());
        commentEntity.setApproved(false);
        commentEntity.setText(request.getText());
        commentRepository.saveAndFlush(commentEntity);
    }

    @Override
    public void delete(Long commentId, Long userId, Authentication auth) {
        if(commentRepository.existsById(commentId)){
            this.commentRepository.deleteById(commentId);
        }else{
            throw new NotFoundException();
        }

    }

}
