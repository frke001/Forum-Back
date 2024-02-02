package org.unibl.etf.forumback.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.forumback.models.dto.PermissionDTO;
import org.unibl.etf.forumback.repositories.PermissionRepository;
import org.unibl.etf.forumback.services.PermissionService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    public PermissionServiceImpl(PermissionRepository permissionRepository, ModelMapper modelMapper) {
        this.permissionRepository = permissionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PermissionDTO> getAll() {
        return permissionRepository.findAll().stream().map(el->modelMapper.map(el, PermissionDTO.class)).collect(Collectors.toList());
    }
}
