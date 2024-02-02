package org.unibl.etf.forumback.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.forumback.models.entities.PermissionEntity;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private List<String> permissions;
    private String token;
}
