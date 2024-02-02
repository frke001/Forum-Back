package org.unibl.etf.forumback.models.entities;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "permission")
public class PermissionEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "name", nullable = false, length = 50)
    private String name;

}
