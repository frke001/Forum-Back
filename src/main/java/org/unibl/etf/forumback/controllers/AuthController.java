package org.unibl.etf.forumback.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.forumback.models.dto.*;
import org.unibl.etf.forumback.services.AuthService;
import org.unibl.etf.forumback.services.OAuth2Service;
import org.unibl.etf.forumback.services.WAFService;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final OAuth2Service oAuth2Service;

    private final WAFService wafService;

    public AuthController(AuthService authService, OAuth2Service oAuth2Service, WAFService wafService) {
        this.authService = authService;
        this.oAuth2Service = oAuth2Service;
        this.wafService = wafService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid UserRegisterDTO request, BindingResult bindingResult){
        wafService.checkValidity(bindingResult);
        authService.register(request);
    }

    @PostMapping("/check-details")
    public boolean checkDetails(@RequestBody DetailsRequestDTO requestDTO){
        return this.authService.checkDetails(requestDTO);
    }

    @PostMapping("/login")
    public Long login(@RequestBody @Valid LoginDTO request, BindingResult bindingResult){
        wafService.checkValidity(bindingResult);
        return authService.login(request);
    }
    @PostMapping("/check-code/{id}")
    public UserDTO checkCode(@PathVariable Long id, @RequestBody @Valid CodeDTO request, BindingResult bindingResult){
        wafService.checkValidity(bindingResult);
        return authService.checkCode(id,request);
    }

    @GetMapping("/oauth2/callback")
    public UserDTO callback(@RequestParam String code) throws URISyntaxException, IOException {
        return oAuth2Service.oAuth2signIn(code);
    }
}
