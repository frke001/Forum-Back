package org.unibl.etf.forumback.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.forumback.models.entities.PermissionEntity;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {

    private Long id;
    private String name;
    private String surname;
    private String username;
    private String role;
    private Boolean blocked;
    private Boolean verified;
    private List<PermissionEntity> permissions;
}
