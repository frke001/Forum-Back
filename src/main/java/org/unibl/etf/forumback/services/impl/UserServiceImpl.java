package org.unibl.etf.forumback.services.impl;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.unibl.etf.forumback.exceptions.NotFoundException;
import org.unibl.etf.forumback.exceptions.UnauthorizedException;
import org.unibl.etf.forumback.models.dto.ChangePermissionDTO;
import org.unibl.etf.forumback.models.dto.ChangeRoleDTO;
import org.unibl.etf.forumback.models.dto.JwtUserDTO;
import org.unibl.etf.forumback.models.dto.UserInfoDTO;
import org.unibl.etf.forumback.models.entities.PermissionEntity;
import org.unibl.etf.forumback.models.entities.UserEntity;
import org.unibl.etf.forumback.models.enums.Role;
import org.unibl.etf.forumback.repositories.PermissionRepository;
import org.unibl.etf.forumback.repositories.UserRepository;
import org.unibl.etf.forumback.services.EmailService;
import org.unibl.etf.forumback.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PermissionRepository permissionRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.permissionRepository = permissionRepository;
        this.emailService = emailService;
    }

    @Override
    public List<UserInfoDTO> getAll(Authentication auth) {
        var jwtUser=(JwtUserDTO) auth.getPrincipal();
        return userRepository.getAllByIdNot(jwtUser.getId()).stream().map(el->modelMapper.map(el,UserInfoDTO.class)).collect(Collectors.toList());
    }

    @Override
    public void blockUnblock(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(NotFoundException::new);
        userEntity.setBlocked(!userEntity.getBlocked());
        userEntity = userRepository.saveAndFlush(userEntity);
    }

    @Override
    public void approve(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(NotFoundException::new);
        userEntity.setVerified(true);
        userEntity = userRepository.saveAndFlush(userEntity);
        emailService.sendEmail("Your account is approved!","Account approved",userEntity.getMail());
    }

    @Override
    public void changeRole(Long id, ChangeRoleDTO request) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(NotFoundException::new);
        userEntity.setRole(Role.getByRole(request.getRole()));
        userEntity = userRepository.saveAndFlush(userEntity);
    }

    @Override
    public void changePermissions(Long id, ChangePermissionDTO request) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(NotFoundException::new);
        userEntity.getPermissions().clear();
        request.getPermissions().forEach(el->{
            PermissionEntity permissionEntity = permissionRepository.findById(el).orElseThrow(NotFoundException::new);
            userEntity.getPermissions().add(permissionEntity);
        });
        userRepository.saveAndFlush(userEntity);
    }
}
