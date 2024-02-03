package org.unibl.etf.forumback.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.unibl.etf.forumback.models.enums.Role;

import java.util.List;

@Data
@Entity
@Table(name = "user")
public class UserEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Basic
    @Column(name = "surname", nullable = false, length = 50)
    private String surname;

    @Basic
    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Basic
    @Column(name = "password", length = 512)
    private String password;

    @Basic
    @Column(name = "mail", nullable = false, length = 50, unique = true)
    private String mail;

    @Basic
    @Column(name = "verified", nullable = false)
    private Boolean verified;

    @Basic
    @Column(name = "blocked", nullable = false)
    private Boolean blocked;

    @Basic
    @Column(name = "code", length = 4)
    private String code;

    @Enumerated(EnumType.ORDINAL)
    @Column
    private Role role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<PermissionEntity> permissions;
}
