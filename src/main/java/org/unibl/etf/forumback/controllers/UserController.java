package org.unibl.etf.forumback.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.forumback.models.dto.ChangePermissionDTO;
import org.unibl.etf.forumback.models.dto.ChangeRoleDTO;
import org.unibl.etf.forumback.models.dto.UserInfoDTO;
import org.unibl.etf.forumback.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public List<UserInfoDTO> getAll(Authentication auth){
        return userService.getAll(auth);
    }

    @PutMapping("/block/{id}")
    public void blockUnblock(@PathVariable Long id){
        this.userService.blockUnblock(id);
    }

    @PutMapping("/approve/{id}")
    public void approve(@PathVariable Long id){
        this.userService.approve(id);
    }
    @PutMapping("/role/{id}")
    public void changeRole(@PathVariable Long id, @RequestBody @Valid ChangeRoleDTO request){
        this.userService.changeRole(id, request);
    }
    @PutMapping("/permission/{id}")
    public void changePermissions(@PathVariable Long id, @RequestBody ChangePermissionDTO request){
        this.userService.changePermissions(id, request);
    }
}
