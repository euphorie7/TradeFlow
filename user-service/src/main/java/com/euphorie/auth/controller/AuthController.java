package com.euphorie.auth.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

import com.euphorie.auth.service.AuthService;
import com.euphorie.auth.dto.SignUpDto;
import com.euphorie.auth.dto.SignInDto;
import com.euphorie.user.dto.UserResponseDto;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // POST /auth/signup
    @PostMapping("/signup")
    public UserResponseDto signUp(
            @Valid @RequestBody SignUpDto dto
    ) {
        return authService.signUp(dto);
    }


    // POST /auth/signin
    @PostMapping("/signin")
    public String signIn(
            @Valid @RequestBody SignInDto dto
    ) {
        return authService.signIn(dto);
    }
}