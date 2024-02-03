package org.unibl.etf.forumback.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "comment")
public class CommentEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "text", nullable = false, length = 1000)
    private String text;

    @Basic
    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @Basic
    @Column(name = "approved", nullable = false)
    private Boolean approved;

    @ManyToOne
    @JoinColumn(name = "user_sender", referencedColumnName = "id")
    private UserEntity userSender;

    @ManyToOne
    @JoinColumn(name = "forum_category", referencedColumnName = "id")
    private ForumCategoryEntity category;
}
