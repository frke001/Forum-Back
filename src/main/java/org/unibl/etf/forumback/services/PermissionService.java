package org.unibl.etf.forumback.services;

import org.unibl.etf.forumback.models.dto.PermissionDTO;

import java.util.List;

public interface PermissionService {

    List<PermissionDTO> getAll();
}
