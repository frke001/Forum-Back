package org.unibl.etf.forumback.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.forumback.models.dto.*;
import org.unibl.etf.forumback.services.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid UserRegisterDTO request){
        authService.register(request);
    }

    @PostMapping("/check-details")
    public boolean checkDetails(@RequestBody DetailsRequestDTO requestDTO){
        return this.authService.checkDetails(requestDTO);
    }

    @PostMapping("/login")
    public Long login(@RequestBody @Valid LoginDTO request){
        return authService.login(request);
    }
    @PostMapping("/check-code/{id}")
    public UserDTO checkCode(@PathVariable Long id, @RequestBody @Valid CodeDTO request){
        return authService.checkCode(id,request);
    }
}
