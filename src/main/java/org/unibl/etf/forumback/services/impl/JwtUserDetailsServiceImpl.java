package org.unibl.etf.forumback.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.unibl.etf.forumback.exceptions.UnauthorizedException;
import org.unibl.etf.forumback.models.dto.JwtUserDTO;
import org.unibl.etf.forumback.repositories.UserRepository;
import org.unibl.etf.forumback.services.JwtUserDetailsService;

@Service
public class JwtUserDetailsServiceImpl implements JwtUserDetailsService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public JwtUserDetailsServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return modelMapper.map(userRepository.findByUsername(username).orElseThrow(UnauthorizedException::new), JwtUserDTO.class);
    }
}
