package org.unibl.etf.forumback.services.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.unibl.etf.forumback.exceptions.AccountBlockedException;
import org.unibl.etf.forumback.exceptions.NotApprovedException;
import org.unibl.etf.forumback.exceptions.NotFoundException;
import org.unibl.etf.forumback.exceptions.UnauthorizedException;
import org.unibl.etf.forumback.models.dto.*;
import org.unibl.etf.forumback.models.entities.UserEntity;
import org.unibl.etf.forumback.models.enums.Role;
import org.unibl.etf.forumback.repositories.UserRepository;
import org.unibl.etf.forumback.services.AuthService;
import org.unibl.etf.forumback.services.EmailService;
import org.unibl.etf.forumback.services.JwtService;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    private final EmailService emailService;

    private final JwtService jwtService;

    @PersistenceContext
    private EntityManager entityManager;

    public AuthServiceImpl(ModelMapper modelMapper, PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, EmailService emailService, JwtService jwtService) {
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.jwtService = jwtService;
    }

    @Override
    public void register(UserRegisterDTO request) {
        UserEntity entity = modelMapper.map(request, UserEntity.class);
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        entity.setVerified(false);
        entity.setBlocked(false);
        entity.setRole(Role.CLIENT);
        entity = userRepository.saveAndFlush(entity);
        entityManager.refresh(entity);
    }

    @Override
    public boolean checkDetails(DetailsRequestDTO detailsRequestDTO) {
        if(detailsRequestDTO.getMail() != null){
            return userRepository.existsByMail(detailsRequestDTO.getMail());
        }else if(detailsRequestDTO.getUsername() != null){
            return userRepository.existsByUsername(detailsRequestDTO.getUsername());
        }
        return false;
    }

    @Override
    public Long login(LoginDTO request) {

        Authentication auth = null;
        try {
            auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        }catch (Exception ex){
            throw new UnauthorizedException();
        }
        JwtUserDTO user = (JwtUserDTO) auth.getPrincipal();
        UserEntity userEntity = userRepository.findById(user.getId()).orElseThrow(NotFoundException::new);
        if(!userEntity.getVerified()){
            throw new NotApprovedException();
        }
        if(userEntity.getBlocked()){
            throw new AccountBlockedException();
        }
        Random random = new Random();
        int code = random.nextInt(9000) + 1000;
        userEntity.setCode(String.valueOf(code));
        emailService.sendEmail("Your authentication code is " + code + ".","Authentication code", userEntity.getMail());
        return userEntity.getId();
    }

    @Override
    public UserDTO checkCode(Long id, CodeDTO request) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(NotFoundException::new);
        UserDTO response = new UserDTO();
        if(userEntity.getCode() == null){
            throw new UnauthorizedException();
        }
        if(userEntity.getCode().equals(request.getCode())){
            JwtUserDTO jwtUser = modelMapper.map(userEntity, JwtUserDTO.class);
            var token = jwtService.generateToken(jwtUser);
            response.setToken(token);
            List<String> permissions = userEntity.getPermissions().stream().map(el->el.getName()).collect(Collectors.toList());
            response.setPermissions(permissions);
            userEntity.setCode(null);
            return response;
        }else{
            throw new UnauthorizedException();
        }
    }

}
