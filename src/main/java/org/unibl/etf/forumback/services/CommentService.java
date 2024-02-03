package org.unibl.etf.forumback.services;

import org.springframework.security.core.Authentication;
import org.unibl.etf.forumback.models.dto.AcceptCommentDTO;
import org.unibl.etf.forumback.models.dto.RequestCommentDTO;
import org.unibl.etf.forumback.models.dto.ResponseCommentDTO;

import java.util.List;

public interface CommentService {

    List<ResponseCommentDTO> getAll(Long id);
    void insert(Long id, RequestCommentDTO request, Authentication auth);

    void update(Long id, RequestCommentDTO request, Authentication auth);
    void delete(Long commentId, Long userId, Authentication auth);
    List<ResponseCommentDTO> getAllNotApproved();

    void accept(Long id,AcceptCommentDTO request);
    void forbid(Long id);
}
