package org.unibl.etf.forumback.services;

import org.springframework.security.core.Authentication;
import org.unibl.etf.forumback.models.dto.ChangePermissionDTO;
import org.unibl.etf.forumback.models.dto.ChangeRoleDTO;
import org.unibl.etf.forumback.models.dto.UserInfoDTO;

import java.util.List;

public interface UserService {

    List<UserInfoDTO> getAll(Authentication auth);

    void blockUnblock(Long id);
    void approve(Long id);

    void changeRole(Long id, ChangeRoleDTO request);
    void changePermissions(Long id, ChangePermissionDTO request);
}
