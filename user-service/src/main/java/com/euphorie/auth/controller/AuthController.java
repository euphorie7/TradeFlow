package com.euphorie.auth.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

import com.euphorie.auth.service.AuthService;
import com.euphorie.auth.dto.SignUpDto;
import com.euphorie.auth.dto.SignInDto;
import com.euphorie.user.dto.UserResponseDto;


import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.access.prepost.PreAuthorize;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public Map<String, Object> me() {

        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

        Jwt jwt = (Jwt) authentication.getPrincipal();

        return jwt.getClaims();
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