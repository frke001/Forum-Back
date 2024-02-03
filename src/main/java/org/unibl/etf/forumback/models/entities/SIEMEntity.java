package org.unibl.etf.forumback.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "siem")
public class SIEMEntity {

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
}
