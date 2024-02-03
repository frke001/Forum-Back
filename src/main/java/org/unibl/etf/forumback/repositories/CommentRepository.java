package org.unibl.etf.forumback.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.unibl.etf.forumback.models.entities.CommentEntity;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query("SELECT c FROM CommentEntity c WHERE c.category.id = :categoryId AND c.approved = true ORDER BY c.creationDate DESC")
    List<CommentEntity> findTop20ByCategoryOrderByCreationDateDesc(@Param("categoryId") Long categoryId, Pageable pageable);
}
